package dev.woc.afterline.common.net.message;

import io.netty.channel.Channel;

@FunctionalInterface
public interface MessageHandler {

    void handle(Channel netChannel, Message message);
}

