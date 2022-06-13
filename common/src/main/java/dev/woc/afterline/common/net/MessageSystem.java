package dev.woc.afterline.common.net;


import dev.woc.afterline.common.net.message.Message;
import dev.woc.afterline.common.net.message.MessageHandler;
import dev.woc.afterline.common.net.message.MessageID;
import dev.woc.afterline.common.net.message.SubscribeMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public final class MessageSystem {
    public static final Logger LOGGER = LogManager.getLogger("MessageSystem");
    private static Map<Integer, List<MessageHandler>> messageHandlers = new HashMap<>();

    private void initAllFrom(Class<?> clazz) {
        // scan class
        Arrays.stream(clazz.getMethods()).filter(method -> method.isAnnotationPresent(SubscribeMessage.class) && Modifier.isStatic(method.getModifiers()) && verifyArgs(method)).forEach(method -> {
            if (method.getAnnotation(SubscribeMessage.class).id() == -1) {
                Class<?> msg = method.getParameterTypes()[1];
                if (msg.isAnnotationPresent(MessageID.class)) registerHandler(createMsgHandler(msg, method), msg.getAnnotation(MessageID.class).id());
                else registerHandler(createMsgHandler(msg, method), Message.getMessageID(msg));
            } else {
                registerHandler((netChannel, message) -> {
                    try {
                        method.invoke(null, netChannel, message);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        LOGGER.catching(e);
                        LOGGER.error("Failed to invoke message handler {}", method.getName());
                    }
                }, method.getAnnotation(SubscribeMessage.class).id());
            }
        });
    }

    private MessageHandler createMsgHandler(Class<?> msg, Method method) {
        return (netChannel, message) -> {
            try {
                method.invoke(null, netChannel, msg.cast(message));
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.catching(e);
                LOGGER.error("Failed to invoke message handler {}", method.getName());
            }
        };
    }

    private boolean verifyArgs(Method method) {
        return method.getParameterTypes().length == 2 && method.getParameterTypes()[0].isAssignableFrom(Channel.class) && Message.class.isAssignableFrom(method.getParameterTypes()[1]);
    }

    public void postMessage(Channel channel, Message message) {
        ByteBuf buf = message.writeBuffer(channel, channel.alloc());
        try {
            channel.writeAndFlush(buf).sync();
        } catch (InterruptedException e) {
            LOGGER.catching(e);
        }
    }

    public static void registerHandler(MessageHandler hndlr, int msgID) {
        if (msgID < 0) {
            LOGGER.error("Cannot register a message handler for message with id {}", msgID);
            return;
        }

        if (!Message.exists(msgID)) {
            LOGGER.error("Cannot register a message handler for message with id {}", msgID);
            return;
        }

        messageHandlers.putIfAbsent(msgID, new ArrayList<>());
        messageHandlers.get(msgID).add(hndlr);
        LOGGER.debug("Registered messsage handler for message id {}", msgID);
    }

    public void processData(Channel ch, ByteBuf in) {
        int msgID = in.readInt();
        long msgSize = in.readInt();

        LOGGER.debug("Received message with id {}", msgID);

        Optional<Message> messageOpt = Optional.ofNullable(Message.create(msgID, msgSize, in));
        messageOpt.ifPresentOrElse(msg_ -> {
            messageHandlers.getOrDefault(msgID, List.of()).forEach(mh -> mh.handle(ch, msg_));
        }, () -> LOGGER.error("Cannot process message: failed to create a message object from message data"));

    }
}
