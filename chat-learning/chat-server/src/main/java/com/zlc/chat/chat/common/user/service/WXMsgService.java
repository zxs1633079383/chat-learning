package com.zlc.chat.chat.common.user.service;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--15:08
 * 3. 目的:
 */

public interface WXMsgService {
    WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage);

    //用户授权
    void authorize(WxOAuth2UserInfo userInfo);
}
