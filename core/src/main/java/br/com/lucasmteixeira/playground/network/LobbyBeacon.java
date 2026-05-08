package br.com.lucasmteixeira.playground.network;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.gson.Gson;

import br.com.lucasmteixeira.playground.model.LobbyState;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultThreadFactory;

public class LobbyBeacon {
    public static final int BEACON_PORT = 7778;
    public static final String MULTICAST_GROUP = "239.255.77.78";
    private static final int INTERVAL_MS = 2000;
    private static final Logger LOG = Logger.getLogger(LobbyBeacon.class.getName());
    private static final Gson GSON = new Gson();

    public static class BeaconPayload {
        public String hostIp;
        public String adventureName;
        public String hostCharacterName;
        public int playerCount;
        public int gamePort;

        public BeaconPayload() {}

        public BeaconPayload(String hostIp, LobbyState lobby) {
            this.hostIp = hostIp;
            this.adventureName = lobby.adventureName;
            this.hostCharacterName = lobby.players.isEmpty() ? "" : lobby.players.get(0).characterName;
            this.playerCount = lobby.players.size();
            this.gamePort = NetworkHost.DEFAULT_PORT;
        }
    }

    private final LobbyState lobbyState;
    private EventLoopGroup group;
    private Channel channel;

    public LobbyBeacon(LobbyState lobbyState) {
        this.lobbyState = lobbyState;
    }

    public void start() {
        group = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-beacon", true));
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInboundHandlerAdapter());

            channel = b.bind(0).sync().channel();
            String localIp = InetAddress.getLocalHost().getHostAddress();
            InetSocketAddress multicastAddr = new InetSocketAddress(MULTICAST_GROUP, BEACON_PORT);
            LOG.info("[BEACON] Started multicast on " + MULTICAST_GROUP + ":" + BEACON_PORT + " from " + localIp);

            channel.eventLoop().scheduleAtFixedRate(() -> {
                if (!channel.isActive()) return;
                BeaconPayload payload = new BeaconPayload(localIp, lobbyState);
                String json = GSON.toJson(payload);
                DatagramPacket packet = new DatagramPacket(
                    Unpooled.copiedBuffer(json, CharsetUtil.UTF_8), multicastAddr);
                channel.writeAndFlush(packet);
                LOG.fine("[BEACON] Sent: adventure=" + lobbyState.adventureName + ", players=" + payload.playerCount);
            }, 0, INTERVAL_MS, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            LOG.severe("[BEACON] Error: " + e.getMessage());
            if (group != null) group.shutdownGracefully();
        }
    }

    public void stop() {
        LOG.info("[BEACON] Stopping...");
        if (channel != null) channel.close();
        if (group != null) group.shutdownGracefully();
    }
}
