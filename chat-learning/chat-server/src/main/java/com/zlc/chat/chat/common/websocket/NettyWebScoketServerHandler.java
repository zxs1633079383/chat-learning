package com.zlc.chat.chat.common.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.zlc.chat.chat.common.websocket.domain.enums.WSReqTypeEnum;
import com.zlc.chat.chat.common.websocket.domain.vo.req.WSBaseReq;
import com.zlc.chat.chat.common.websocket.service.WebSocketService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/8--17:37
 * 3. 目的:
 */

@ChannelHandler.Sharable
@Slf4j
public class NettyWebScoketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private WebSocketService webSocketService;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        //1. 建立连接
        webSocketService.connect(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("用户断开连接, 下线");
        userOffline(ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            String token = NettyUtils.getAttr(ctx.channel(), NettyUtils.TOKEN);
            if(StrUtil.isNotBlank(token)){
                webSocketService.authorize(ctx.channel(),token);
            }
            log.error("握手完成 token: " + token);
        } else if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {

                System.out.println("读空闲");
                //todo 用户下线
                log.error("读空闲,用户下线");
                userOffline(ctx.channel());
//                ctx.channel().close();
            }
        } else if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            log.info("自定义protocol 握手请求进入");
            //获取channel绑定的token
            Attribute<Object> token = ctx.channel().attr(AttributeKey.valueOf("token"));
            String token_value = token.get().toString();
            webSocketService.authorize(ctx.channel(), token_value);
        }
    }

    /**
     * 用户下线统一处理
     *
     * @param channel
     */
    private void userOffline(Channel channel) {
        webSocketService.offLine(channel);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常捕获:  netty Handle exception" + cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        log.error("发送信息为: " + text);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())) {
            case AUTHORIZE:
                webSocketService.authorize(ctx.channel(), wsBaseReq.getData());
                break;

            case LOGIN:
                webSocketService.handleLoginReq(ctx.channel());
                log.error("请求二维码");
                break;
            case HEARTBEAT:
                break;
        }
        System.out.println(text);
    }
}
