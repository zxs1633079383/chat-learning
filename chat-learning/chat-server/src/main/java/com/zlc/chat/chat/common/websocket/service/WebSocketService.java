package com.zlc.chat.chat.common.websocket.service;

import com.zlc.chat.chat.common.websocket.domain.vo.resp.WSBaseResp;
import io.netty.channel.Channel;

public interface WebSocketService {
    void connect(Channel channel);

    void handleLoginReq(Channel channel);

    void offLine(Channel channel);

    //通过二维码 让用户登录
    void scanLoginSuccess(Integer code, Long id);

    /***
     * 等待授权
     * @param code
     */
    void waiteAuthorize(Integer code);

    /**
     * 验证token
     * @param channel
     * @param data
     */
    void authorize(Channel channel, String data);

    void sendMsgToAll(WSBaseResp<?> msg);
}
