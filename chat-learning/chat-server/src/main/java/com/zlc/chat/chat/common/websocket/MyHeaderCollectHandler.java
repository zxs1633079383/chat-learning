package com.zlc.chat.chat.common.websocket;

import cn.hutool.core.net.url.UrlBuilder;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.EventExecutorGroup;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.Optional;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--9:25
 * 3. 目的: websocket链接携带token 降低链接次数
 */

public class MyHeaderCollectHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpObject httpObject = null;
        if (msg instanceof HttpObject) {
            httpObject = (HttpObject) msg;
        }


        if (httpObject instanceof HttpRequest) {

            HttpRequest request = (HttpRequest) httpObject;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.getUri());
            Optional<String> tokenOptional = Optional.ofNullable(urlBuilder)
                    .map(UrlBuilder::getQuery)
                    .map(k -> k.get("token"))
                    .map(CharSequence::toString);

            tokenOptional.ifPresent(s -> NettyUtils.setAttr(ctx.channel(), NettyUtils.TOKEN, s));
            request.setUri(urlBuilder.getPath().toString());
            //Nginx 用户IP
            String ip = request.headers().get("X-Real-IP");
            if(StringUtils.isBlank(ip)){
                InetSocketAddress address =  (InetSocketAddress)ctx.channel().remoteAddress();
               ip = address.getAddress().getHostAddress();
            }
            //保存到Channel附件
            NettyUtils.setAttr(ctx.channel(), NettyUtils.IP, ip);
            ctx.pipeline().remove(this);
        }

        ctx.fireChannelRead(msg);

    }
}
