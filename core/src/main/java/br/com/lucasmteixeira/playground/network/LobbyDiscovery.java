package br.com.lucasmteixeira.playground.network;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.google.gson.Gson;

import br.com.lucasmteixeira.playground.network.LobbyBeacon.BeaconPayload;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultThreadFactory;

public class LobbyDiscovery {
    private static final long EXPIRY_MS = 6000;
    private static final Logger LOG = Logger.getLogger(LobbyDiscovery.class.getName());
    private static final Gson GSON = new Gson();

    private final Map<String, BeaconPayload> discovered = new ConcurrentHashMap<>();
    private final Map<String, Long> lastSeen = new ConcurrentHashMap<>();
    private final Consumer<Map<String, BeaconPayload>> onUpdate;

    private EventLoopGroup group;
    private Channel channel;

    public LobbyDiscovery(Consumer<Map<String, BeaconPayload>> onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void start() {
        group = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-discovery", true));
        try {
            InetAddress groupAddr = InetAddress.getByName(LobbyBeacon.MULTICAST_GROUP);
            NetworkInterface netIf = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());

            Bootstrap b = new Bootstrap();
            b.group(group)
                .channelFactory(() -> new NioDatagramChannel(InternetProtocolFamily.IPv4))
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.IP_MULTICAST_IF, netIf)
                .localAddress(new InetSocketAddress(LobbyBeacon.BEACON_PORT))
                .handler(new DiscoveryHandler());

            channel = b.bind().sync().channel();
            ((NioDatagramChannel) channel).joinGroup(
                new InetSocketAddress(groupAddr, LobbyBeacon.BEACON_PORT), netIf).sync();

            LOG.info("[DISCOVERY] Joined multicast group " + LobbyBeacon.MULTICAST_GROUP + ":" + LobbyBeacon.BEACON_PORT);

            // Cleanup expired lobbies
            channel.eventLoop().scheduleAtFixedRate(() -> {
                long now = System.currentTimeMillis();
                boolean changed = lastSeen.entrySet().removeIf(e -> {
                    if (now - e.getValue() > EXPIRY_MS) {
                        LOG.info("[DISCOVERY] Lobby expired: host=" + e.getKey());
                        discovered.remove(e.getKey());
                        return true;
                    }
                    return false;
                });
                if (changed) onUpdate.accept(discovered);
            }, 1, 1, TimeUnit.SECONDS);

        } catch (Exception e) {
            LOG.severe("[DISCOVERY] Error: " + e.getMessage());
            if (group != null) group.shutdownGracefully();
        }
    }

    public void stop() {
        LOG.info("[DISCOVERY] Stopping...");
        if (channel != null) channel.close();
        if (group != null) group.shutdownGracefully();
    }

    private class DiscoveryHandler extends SimpleChannelInboundHandler<DatagramPacket> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
            String json = packet.content().toString(CharsetUtil.UTF_8);
            BeaconPayload payload = GSON.fromJson(json, BeaconPayload.class);
            boolean isNew = !discovered.containsKey(payload.hostIp);
            discovered.put(payload.hostIp, payload);
            lastSeen.put(payload.hostIp, System.currentTimeMillis());
            if (isNew) {
                LOG.info("[DISCOVERY] New lobby: host=" + payload.hostIp + ", adventure=" + payload.adventureName + ", players=" + payload.playerCount);
            } else {
                LOG.fine("[DISCOVERY] Beacon refresh from " + payload.hostIp);
            }
            onUpdate.accept(discovered);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            LOG.severe("[DISCOVERY] Error: " + cause.getMessage());
        }
    }
}
