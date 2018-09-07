package com.daemonauth.export.rpc.annotion;

import java.lang.annotation.*;

/**
 * User:
 * Date: 15-1-27
 * Time: 下午8:00
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String path() default "";

    RequestMethod method() default RequestMethod.GET;
}