package dev.woc.afterline.common.net.message.base;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface SubscribeMessage {
    int id() default -1;
}
