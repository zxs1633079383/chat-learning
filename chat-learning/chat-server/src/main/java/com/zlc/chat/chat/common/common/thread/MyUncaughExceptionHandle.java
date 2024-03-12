package com.zlc.chat.chat.common.common.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--8:40
 * 3. 目的:
 */

@Slf4j
public class MyUncaughExceptionHandle implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in Thread ",e);
    }
}
