package dev.woc.afterline.client.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutorGroup;

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
}
