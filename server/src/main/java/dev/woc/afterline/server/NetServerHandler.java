package dev.woc.afterline.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutorGroup;

public class NetServerHandler extends ChannelInboundHandlerAdapter {
    private NetServer net;

    public NetServerHandler(NetServer ns) {
        this.net = ns;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            net.getMessageSystem().processData(ctx.channel(), (ByteBuf) msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
