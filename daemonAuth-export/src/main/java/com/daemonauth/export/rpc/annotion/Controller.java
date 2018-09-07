package com.daemonauth.export.rpc.annotion;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * User:
 * Date: 15-1-27
 * Time: 下午7:58
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
    String value() default "";
}
