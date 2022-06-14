package dev.woc.afterline.common.net.message;

import dev.woc.afterline.common.net.message.base.BlankMessage;
import dev.woc.afterline.common.net.message.base.Message;
import dev.woc.afterline.common.net.message.base.MessageID;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

@MessageID(id = 2)
public class ConnectionCheck extends BlankMessage {
}
