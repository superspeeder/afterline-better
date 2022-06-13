package dev.woc.afterline.server;

import dev.woc.afterline.common.net.message.PingMessage;
import dev.woc.afterline.common.net.message.base.SubscribeMessage;
import io.netty.channel.Channel;

public class SimpleHandlers {

    @SubscribeMessage
    public static void onPing(Channel ch, PingMessage pingMessage) {
        Afterline.LOGGER.info("Ping! ({})", ch.remoteAddress());
    }
}