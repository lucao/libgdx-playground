package br.com.lucasmteixeira.playground.network;

import java.net.URI;
import java.util.logging.Logger;

import com.google.gson.Gson;

import br.com.lucasmteixeira.playground.model.NetMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.concurrent.DefaultThreadFactory;

public class NetworkClient {
    private static final Gson GSON = new Gson();
    private static final Logger LOG = Logger.getLogger(NetworkClient.class.getName());

    private final String hostIp;
    private final int port;
    private final NetworkListener listener;
    private EventLoopGroup group;
    private Channel channel;

    public NetworkClient(String hostIp, NetworkListener listener) {
        this.hostIp = hostIp;
        this.port = NetworkHost.DEFAULT_PORT;
        this.listener = listener;
    }

    public void connect() {
        LOG.info("[CLIENT] Attempting connection to ws://" + hostIp + ":" + port);
        group = new NioEventLoopGroup(0, new DefaultThreadFactory("netty-client", true));
        try {
            URI uri = new URI("ws://" + hostIp + ":" + port + "/");
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());

            ClientHandler handler = new ClientHandler(handshaker);

            Bootstrap b = new Bootstrap();
            b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                            new HttpClientCodec(),
                            new HttpObjectAggregator(65536),
                            handler
                        );
                    }
                });

            channel = b.connect(hostIp, port).sync().channel();
            handler.handshakeFuture().sync();
            LOG.info("[CLIENT] Connected to host");
        } catch (Exception e) {
            LOG.severe("[CLIENT] Connection failed: " + e.getMessage());
            if (group != null) group.shutdownGracefully();
        }
    }

    public void send(NetMessage msg) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(new TextWebSocketFrame(GSON.toJson(msg)));
        }
    }

    public boolean isOpen() {
        return channel != null && channel.isActive();
    }

    public void close() {
        try {
            if (channel != null) channel.close().sync();
            if (group != null) group.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        LOG.info("[CLIENT] Closed");
    }

    private class ClientHandler extends SimpleChannelInboundHandler<Object> {
        private final WebSocketClientHandshaker handshaker;
        private ChannelPromise handshakePromise;

        ClientHandler(WebSocketClientHandshaker handshaker) {
            this.handshaker = handshaker;
        }

        ChannelFuture handshakeFuture() {
            return handshakePromise;
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            handshakePromise = ctx.newPromise();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            handshaker.handshake(ctx.channel());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            LOG.info("[CLIENT] Disconnected");
            listener.onDisconnected();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
            if (!handshaker.isHandshakeComplete()) {
                handshaker.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
                handshakePromise.setSuccess();
                return;
            }
            if (msg instanceof TextWebSocketFrame) {
                String text = ((TextWebSocketFrame) msg).text();
                NetMessage netMsg = GSON.fromJson(text, NetMessage.class);
                listener.onMessage(netMsg);
            } else if (msg instanceof CloseWebSocketFrame) {
                ctx.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            LOG.severe("[CLIENT] Error: " + cause.getClass().getSimpleName() + " - " + cause.getMessage());
            if (!handshakePromise.isDone()) handshakePromise.setFailure(cause);
            ctx.close();
        }
    }
}
