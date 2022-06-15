package dev.woc.afterline.common;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class Utils {

    public static String readNextString(ByteBuf msgData) {
        int len = msgData.readInt();
        return msgData.readCharSequence(len, StandardCharsets.UTF_8).toString();
    }

    public static void writeString(ByteBuf buf, CharSequence str) {
        buf.writeInt(str.length());
        buf.writeCharSequence(str, StandardCharsets.UTF_8);
    }


    /**
     * Currently supported types:
     *  - CharSequence
     *  - Integer
     *  - Float
     *  - Long
     *  - Double
     *  - Byte
     *  - Character
     *
     *
     * @param objects
     * @return
     */
    public static int calculateMessageLength(Object... objects) {
        int len = 0;

        for (Object obj : objects) {
            if (obj instanceof CharSequence) {
                len += Integer.BYTES + ((CharSequence)obj).length();
            } else if (obj instanceof Integer) {
                len += Integer.BYTES;
            } else if (obj instanceof Float) {
                len += Float.BYTES;
            } else if (obj instanceof Long) {
                len += Long.BYTES;
            } else if (obj instanceof Double) {
                len += Double.BYTES;
            } else if (obj instanceof Byte) {
                len += Byte.BYTES;
            } else if (obj instanceof Character) {
                len += Character.BYTES;
            }
        }

        return len;
    }
}