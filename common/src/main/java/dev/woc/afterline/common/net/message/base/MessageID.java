package dev.woc.afterline.common.net.message.base;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface MessageID {
    int id();
}