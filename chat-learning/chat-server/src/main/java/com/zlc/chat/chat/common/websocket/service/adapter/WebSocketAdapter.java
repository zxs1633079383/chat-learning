package com.zlc.chat.chat.common.websocket.service.adapter;

import com.zlc.chat.chat.common.common.constant.YesOrNoEnum;
import com.zlc.chat.chat.common.user.domain.entity.User;
import com.zlc.chat.chat.common.websocket.domain.enums.WSRespTypeEnum;
import com.zlc.chat.chat.common.websocket.domain.vo.resp.WSBaseResp;
import com.zlc.chat.chat.common.websocket.domain.vo.resp.WSBlack;
import com.zlc.chat.chat.common.websocket.domain.vo.resp.WSLoginSuccess;
import com.zlc.chat.chat.common.websocket.domain.vo.resp.WSLoginUrl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--14:48
 * 3. 目的:
 */

@Slf4j
public class WebSocketAdapter {
    public static WSBaseResp<?> buildResp(WxMpQrCodeTicket wxMpQrCodeTicket)
    {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        resp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));
        return  resp;
    }

    public static WSBaseResp<?> buildResp(User user, String token, boolean power) {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess build = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .uid(user.getId())
                .power(power ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus())
                .build();
        resp.setData(build);


        return  resp;
    }

    public static WSBaseResp<?> buildWaitAuthorizeResp() {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return  resp;
    }

    public static WSBaseResp<?> buildInValidTokenResp() {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return  resp;
    }

    public static WSBaseResp<?> buildBlack(User user) {
        WSBaseResp<WSBlack> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.BLACK.getType());
        WSBlack black = WSBlack.builder()
                        .uid(user.getId())
                                .build();

        resp.setData(black);

        return  resp;
    }
}
