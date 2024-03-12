package com.zlc.chat.chat.common.user.service.handler;

import com.zlc.chat.chat.common.user.service.WXMsgService;
import com.zlc.chat.chat.common.user.service.adapter.TextBuilder;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.Map;

@Component
@Slf4j
public class ScanHandler extends AbstractHandler {


    @Autowired
    private WXMsgService wxMsgService;

    @Value("${wx.mp.callback}")
    private String callback;

    public static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {

        // 拦截
        log.error("handle Service: " + wxMsgService+ "  "+wxMpXmlMessage);
        return wxMsgService.scan(wxMpXmlMessage);

//        return wxMsgService.scan(wxMpService, wxMpXmlMessage);


//        return TextBuilder.build("请点击登录: <a href=" + authorizeUrl + ">登录</a>", wxMpXmlMessage);
//
    }

}
