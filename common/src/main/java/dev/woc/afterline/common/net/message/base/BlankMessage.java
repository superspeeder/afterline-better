package dev.woc.afterline.common.net.message.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

public abstract class BlankMessage extends Message {
    @Override
    protected void readFromBuffer(long msgSize, ByteBuf msgData) {
    }

    @Override
    public ByteBuf writeBuffer(Channel ch, ByteBufAllocator alloc) {
        return createBuffer(alloc, getID(), 0);
    }
}
