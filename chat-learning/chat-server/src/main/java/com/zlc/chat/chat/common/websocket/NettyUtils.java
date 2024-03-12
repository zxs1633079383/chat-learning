package com.zlc.chat.chat.common.websocket;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--10:00
 * 3. 目的:
 */

public class NettyUtils {

    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");
    public static AttributeKey<String> IP = AttributeKey.valueOf("ip");



    public static <T> void setAttr(Channel channel, AttributeKey<T> key,T value){
        channel.attr(key).set(value);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> key){
        Attribute<T> attr = channel.attr(key);
        return attr.get();
    }
}
