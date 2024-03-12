package com.zlc.chat.chat.common.common.thread;

import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--8:50
 * 3. 目的:
 */

@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {

    private static final MyUncaughExceptionHandle MY_UNCAUGH_EXCEPTION_HANDLE =  new MyUncaughExceptionHandle();
    private ThreadFactory original;
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = original.newThread(r); //执行spring 线程的自己的创建逻辑
        //额外装饰我们的功能.
        thread.setUncaughtExceptionHandler(MY_UNCAUGH_EXCEPTION_HANDLE);
        return thread;

    }
}
