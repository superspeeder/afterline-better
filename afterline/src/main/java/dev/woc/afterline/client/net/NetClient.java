package dev.woc.afterline.client.net;

import dev.woc.afterline.client.Afterline;
import dev.woc.afterline.common.net.MessageSystem;
import dev.woc.afterline.common.net.message.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.SslProvider;

import javax.net.ssl.SSLEngine;

public class NetClient implements Runnable {
    private static final String DEFAULT_SERVER_ADDR = "afterline.worldofcat.org";
    private static final int DEFAULT_SERVER_PORT = 40020;

    private Channel channel;
    private Afterline client;

    private MessageSystem messageSystem;
    private String serverAddress = DEFAULT_SERVER_ADDR;
    private int serverPort = DEFAULT_SERVER_PORT;

    private static NetClient NETCLIENT;
    private static Thread THREAD;


    public NetClient(Afterline client) {
        this.client = client;
        messageSystem = new MessageSystem();
        NETCLIENT = this;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public void run() {
        try {

            SslContext sslContext = SslContextBuilder.forClient()
                    .sslProvider(SslProvider.OPENSSL)
                    .trustManager(NetClient.class.getClassLoader().getResourceAsStream("/certs/main.pem")).build();

            SSLEngine sslEngine = sslContext.newEngine(PooledByteBufAllocator.DEFAULT);

            EventLoopGroup workerGroup = new NioEventLoopGroup();
            final NetClient nc = this;
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup);
                b.channel(NioSocketChannel.class);
                b.option(ChannelOption.SO_KEEPALIVE, true);
                b.handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new SslHandler(sslEngine));
                        ch.pipeline().addLast(new NetClientHandler(nc));
                    }
                });

                ChannelFuture f = b.connect(serverAddress, serverPort).sync();
                channel = f.channel();

                Afterline.INSTANCE.onConnectToServer();

                channel.closeFuture().sync();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                System.out.println("Gracefully closed connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postMessage(Message message) {
        getMessageSystem().postMessage(channel, message);
    }

    public static void launchAsThread(Afterline client) {
        THREAD = new Thread(new NetClient(client));
        THREAD.start();
    }
}