package dev.woc.afterline.server;

import dev.woc.afterline.common.net.message.ConnectionCheck;
import dev.woc.afterline.common.net.message.GoodbyeMessage;
import dev.woc.afterline.common.net.message.PingMessage;
import dev.woc.afterline.common.net.message.base.SubscribeMessage;
import io.netty.channel.Channel;

public class SimpleHandlers {

    @SubscribeMessage
    public static void onPing(Channel ch, PingMessage pingMessage) {
        Afterline.LOGGER.info("Ping! ({})", ch.remoteAddress());
    }

    @SubscribeMessage
    public static void onConnectCheck(Channel ch, ConnectionCheck msg) {
        NetServer.getInstance().postMessage(ch, new ConnectionCheck());
    }

    @SubscribeMessage
    public static void onGoodbye(Channel ch, GoodbyeMessage msg) {
        Afterline.LOGGER.info("Closing connection with {}", ch.remoteAddress());
        ch.close();
    }
}
