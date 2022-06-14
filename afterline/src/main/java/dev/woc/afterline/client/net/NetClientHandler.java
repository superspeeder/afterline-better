package dev.woc.afterline.client.net;

import dev.woc.afterline.client.Afterline;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.SocketException;

public class NetClientHandler extends ChannelInboundHandlerAdapter {
    private NetClient net;

    public NetClientHandler(NetClient net) {
        this.net = net;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            net.getMessageSystem().processData(ctx.channel(), (ByteBuf) msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof SocketException && cause.getMessage().equals("Connection reset")) {
            Afterline.LOGGER.warn("Connection was reset");
        } else {
            super.exceptionCaught(ctx, cause);
        }

    }
}
