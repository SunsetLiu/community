package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于需要登录访问方法的自定义注解
 */
//注解用在方法上
@Target(ElementType.METHOD)
//作用到运行的时候
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
