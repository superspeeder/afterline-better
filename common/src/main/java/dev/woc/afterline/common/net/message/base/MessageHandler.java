package dev.woc.afterline.common.net.message.base;

import io.netty.channel.Channel;

@FunctionalInterface
public interface MessageHandler {

    void handle(Channel netChannel, Message message);
}

