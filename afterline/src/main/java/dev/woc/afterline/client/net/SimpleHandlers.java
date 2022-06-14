package dev.woc.afterline.client.net;

import dev.woc.afterline.client.Afterline;
import dev.woc.afterline.common.net.message.ConnectionCheck;
import dev.woc.afterline.common.net.message.PingMessage;
import dev.woc.afterline.common.net.message.base.SubscribeMessage;
import io.netty.channel.Channel;

public class SimpleHandlers {

    @SubscribeMessage
    public static void onPing(Channel ch, PingMessage pingMessage) {
        Afterline.LOGGER.info("Ping! ({})", ch.remoteAddress());
    }

    @SubscribeMessage
    public static void onConnectCheckResp(Channel ch, ConnectionCheck msg) {
        Afterline.INSTANCE.receivedConnectionConfirm = true;
    }
}
