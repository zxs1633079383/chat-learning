package com.zlc.chat.chat.common.common.aspect;

import com.zlc.chat.chat.common.common.annotation.RedissonLock;
import com.zlc.chat.chat.common.common.service.LockService;
import com.zlc.chat.chat.common.common.utils.SpElTUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/11--9:04
 * 3. 目的: 分布式锁切面
 */


@Component
@Aspect
@Order(0) //确保比事务注解先执行.
public class RedissonAspect {

    @Autowired
    private LockService lockService;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLock redissonLock){
        Method method =  ((MethodSignature)joinPoint.getSignature()).getMethod();
        String prefix = StringUtils.isBlank(redissonLock.prefixKey())? SpElTUtils.getMethodKey(method) : redissonLock.prefixKey() ;
        String key = SpElTUtils.parSeSpEL(method,joinPoint.getArgs(),redissonLock.key());
        return lockService.executeWithLock(prefix + ":" +key, redissonLock.waitTime(),redissonLock.unit(),joinPoint::proceed);
    }

}
