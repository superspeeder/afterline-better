package dev.woc.afterline.common.net.message;

import dev.woc.afterline.common.Utils;
import dev.woc.afterline.common.net.message.base.MessageID;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import dev.woc.afterline.common.net.message.base.Message;

@MessageID(id = 5)
public class LoginComplete extends Message {

    private String realUsername;

    public LoginComplete() {

    }

    public LoginComplete(String username) {
        realUsername = username;
    }

    @Override
    protected void readFromBuffer(long msgSize, ByteBuf msgData) {
        realUsername = Utils.readNextString(msgData);
    }

    @Override
    public ByteBuf writeBuffer(Channel ch, ByteBufAllocator alloc) {
        ByteBuf buf = createBuffer(alloc, getID(), Utils.calculateMessageLength(realUsername));
        Utils.writeString(buf, realUsername);
        return buf;
    }

    public String getUsername() {
        return realUsername;
    }
}