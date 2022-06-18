package dev.woc.afterline.client.net;

import dev.woc.afterline.client.Afterline;
import dev.woc.afterline.client.Utils;
import dev.woc.afterline.common.net.message.ConnectionCheck;
import dev.woc.afterline.common.net.message.FederatedLoginRequest;
import dev.woc.afterline.common.net.message.LoginComplete;
import dev.woc.afterline.common.net.message.PingMessage;
import dev.woc.afterline.common.net.message.base.SubscribeMessage;
import io.netty.channel.Channel;

import java.io.IOException;
import java.net.URISyntaxException;

public class SimpleHandlers {

    @SubscribeMessage
    public static void onPing(Channel ch, PingMessage pingMessage) {
        Afterline.LOGGER.info("Ping! ({})", ch.remoteAddress());
    }

    @SubscribeMessage
    public static void onConnectCheckResp(Channel ch, ConnectionCheck msg) {
        Afterline.INSTANCE.receivedConnectionConfirm = true;
    }


    @SubscribeMessage
    public static void onRequestFederatedLoginRespond(Channel ch, FederatedLoginRequest resp) {
        try {
            Afterline.LOGGER.info(resp.getLink());
            Utils.openWebpage(resp.getLink());
        } catch (URISyntaxException | IOException e) {
            Afterline.LOGGER.catching(e);
        }
    }

    @SubscribeMessage
    public static void onLoginComplete(Channel ch, LoginComplete resp) {
        Afterline.INSTANCE.setCurrentUser(resp.getUsername());
    }
}
