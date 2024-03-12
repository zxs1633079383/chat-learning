package com.zlc.chat.chat.common.common.utils;

import com.zlc.chat.chat.common.common.domain.dto.RequestInfo;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--15:30
 * 3. 目的: 请求上下文
 */

public class RequestHolder {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo){
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get(){
        return threadLocal.get();
    }

    public static void remove(){
        threadLocal.remove();
    }
}
