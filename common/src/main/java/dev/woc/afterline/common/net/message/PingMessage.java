package dev.woc.afterline.common.net.message;

import dev.woc.afterline.common.net.message.base.Message;
import dev.woc.afterline.common.net.message.base.MessageID;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

@MessageID(id = 1)
public class PingMessage extends Message {
    @Override
    protected void readFromBuffer(long msgSize, ByteBuf msgData) {
    }

    @Override
    public ByteBuf writeBuffer(Channel ch, ByteBufAllocator alloc) {
        return createBuffer(alloc, getID(), 0);
    }
}
