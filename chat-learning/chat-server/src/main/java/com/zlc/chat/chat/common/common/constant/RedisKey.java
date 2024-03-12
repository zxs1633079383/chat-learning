package com.zlc.chat.chat.common.common.constant;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--17:36
 * 3. 目的:
 */

public class RedisKey {
    private static final String BASE_KEY = "chat-server";
    /**
     * 用户Token的key
     */
    public static final String USER_TOKEN_STRING = "userToken:uid_%d";

    public static String getKey(String key, Object... o) {
//        System.out.println("用户的key为: " + BASE_KEY + String.format(key, o));
        return BASE_KEY + String.format(key, o);
    }


}
