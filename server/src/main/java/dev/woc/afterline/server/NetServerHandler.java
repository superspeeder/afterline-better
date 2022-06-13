package dev.woc.afterline.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

public class NetServerHandler extends ChannelInboundHandlerAdapter {
    public NetServerHandler(NetServer ns) {
    }
}
