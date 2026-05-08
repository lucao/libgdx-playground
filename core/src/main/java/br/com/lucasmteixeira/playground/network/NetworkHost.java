package br.com.lucasmteixeira.playground.network;

import java.util.logging.Logger;

import com.google.gson.Gson;

import br.com.lucasmteixeira.playground.model.NetMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NetworkHost {
    public static final int DEFAULT_PORT = 7777;
    private static final Gson GSON = new Gson();
    private static final Logger LOG = Logger.getLogger(NetworkHost.class.getName());

    private final NetworkListener listener;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel serverChannel;
    private final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public NetworkHost(NetworkListener listener) {
        this.listener = listener;
    }

    public void start() {
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("netty-host-boss", true));
        workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("netty-host-worker", true));
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                            new HttpServerCodec(),
                            new HttpObjectAggregator(65536),
                            new WebSocketServerProtocolHandler("/"),
                            new ServerHandler()
                        );
                    }
                });
            serverChannel = b.bind(DEFAULT_PORT).sync().channel();
            LOG.info("[HOST] Server started on port " + DEFAULT_PORT);
        } catch (Exception e) {
            LOG.severe("[HOST] Failed to start: " + e.getMessage());
            if (bossGroup != null) bossGroup.shutdownGracefully();
            if (workerGroup != null) workerGroup.shutdownGracefully();
        }
    }

    public void stop() {
        try {
            if (serverChannel != null) serverChannel.close().sync();
            if (bossGroup != null) bossGroup.shutdownGracefully().sync();
            if (workerGroup != null) workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        LOG.info("[HOST] Server stopped");
    }

    public void broadcast(NetMessage msg) {
        String json = GSON.toJson(msg);
        clients.writeAndFlush(new TextWebSocketFrame(json));
    }

    private class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            clients.add(ctx.channel());
            LOG.info("[HOST] Client connected: " + ctx.channel().remoteAddress() + ", total=" + clients.size());
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) {
            clients.remove(ctx.channel());
            LOG.info("[HOST] Client disconnected: " + ctx.channel().remoteAddress() + ", total=" + clients.size());
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
            String text = frame.text();
            NetMessage msg = GSON.fromJson(text, NetMessage.class);
            // Broadcast to all except sender
            for (Channel ch : clients) {
                if (ch != ctx.channel()) {
                    ch.writeAndFlush(new TextWebSocketFrame(text));
                }
            }
            listener.onMessage(msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            LOG.severe("[HOST] Error on " + ctx.channel().remoteAddress() + ": " + cause.getMessage());
            ctx.close();
        }
    }
}
