package dev.woc.afterline.server;

import dev.woc.afterline.common.net.MessageSystem;
import dev.woc.afterline.common.net.message.ConnectionCheck;
import dev.woc.afterline.common.net.message.GoodbyeMessage;
import dev.woc.afterline.common.net.message.PingMessage;
import dev.woc.afterline.common.net.message.base.Message;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.SslProvider;

import javax.net.ssl.SSLEngine;
import java.nio.file.Path;

public class NetServer implements Runnable {
    public static final int DEFAULT_SERVER_PORT = 40020;
    private Afterline server;

    private MessageSystem messageSystem;

    private int serverPort = DEFAULT_SERVER_PORT;

    private static NetServer NETSERVER;
    private static Thread THREAD;


    public NetServer(Afterline server, int port) {
        this.server = server;
        messageSystem = new MessageSystem();
        NETSERVER = this;

        Message.register(PingMessage.class);
        Message.register(ConnectionCheck.class);
        Message.register(GoodbyeMessage.class);

        messageSystem.initAllFrom(SimpleHandlers.class);
    }

    public static NetServer getInstance() {
        return NETSERVER;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public void run() {
        try {

            SslContext sslContext = SslContextBuilder
                    .forServer(Path.of(System.getenv("AFTERLINE_CERT")).toFile(), Path.of(System.getenv("AFTERLINE_KEY")).toFile())
                    .sslProvider(SslProvider.OPENSSL)
                    .trustManager(Path.of(System.getenv("AFTERLINE_CERT")).toFile()).build();


            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            final NetServer ns = this;
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                SSLEngine sslEngine = sslContext.newEngine(PooledByteBufAllocator.DEFAULT);
                                ch.pipeline().addLast(new SslHandler(sslEngine));
                                ch.pipeline().addLast(new NetServerHandler(ns));
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                ChannelFuture f = b.bind(serverPort).sync();
                Afterline.LOGGER.info("Afterline Server Started");

                Afterline.INSTANCE.onServerStart();

                f.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
                System.out.println("Gracefully closed connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postMessage(Channel channel, Message message) {
        getMessageSystem().postMessage(channel, message);
    }

    public void launchAsThread() {
        THREAD = new Thread(this);
        THREAD.start();
    }
}
