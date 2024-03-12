package com.zlc.chat.chat.common.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedissonLock {

    /***
     * key的前缀 默认取方法全限定名  可以自己指定
     * @return
     */
    String prefixKey() default "";

    /**
     * 支持spring EL表达式
     * @return
     */
    String key();

    /**
     * 等待锁 排队时间, 默认 快速失败
     * @return
     */
    int waitTime() default  -1;

    /**
     * 时间单位 默认毫秒
     * @return
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;


}
