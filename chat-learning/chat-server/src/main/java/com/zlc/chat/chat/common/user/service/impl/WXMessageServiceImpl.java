package com.zlc.chat.chat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlc.chat.chat.common.user.dao.UserDao;
import com.zlc.chat.chat.common.user.domain.entity.User;
import com.zlc.chat.chat.common.user.service.UserService;
import com.zlc.chat.chat.common.user.service.WXMsgService;
import com.zlc.chat.chat.common.user.service.adapter.TextBuilder;
import com.zlc.chat.chat.common.user.service.adapter.UserAdapter;
import com.zlc.chat.chat.common.websocket.service.WebSocketService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--15:09
 * 3. 目的:
 */

@Service
@Slf4j
public class WXMessageServiceImpl implements WXMsgService {

    @Autowired
    private WebSocketService webSocketService;

    /***
     * openid与登录二维码的 连接关系
     */
    private static final ConcurrentHashMap<String, Integer> WAIT_AUTHORIZE_MAP = new ConcurrentHashMap<>();

    @Value("${wx.mp.callback}")
    private String callback;

    public static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    @Autowired
    private UserDao userDao;

    @Autowired

    private UserService userService;

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    /**
     * 用户扫码成功
     *
     * @param wxMpXmlMessage
     * @return
     */
    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage) {
        String fromUser = wxMpXmlMessage.getFromUser();
        Integer code = getEventKey(wxMpXmlMessage);
        if (Objects.isNull(code)) {
            return null;
        }
        //判断当前用户是否存在
        String openId = wxMpXmlMessage.getFromUser();
        User user = userDao.getByOpenId(openId);
        //注册并授权
        boolean registered = Objects.nonNull(user);
        boolean authorized = registered && StrUtil.isNotBlank(user.getAvatar());
        if (registered && authorized) {
            //走登录成功逻辑,通过Code,给Channle 推送消息
            webSocketService.scanLoginSuccess(code,user.getId());

        }
        //用户未注册,就先注册
        if (!registered) {
            User userSave = UserAdapter.buildUserSave(openId);
            Long register = userService.register(userSave);
            log.error("用户没有注册, 注册是否成功: " + register);
        }


        // 推送链接 让用户授权
        WAIT_AUTHORIZE_MAP.put(openId,code);
        webSocketService.waiteAuthorize(code);
        String authorizeUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + "wx/portal/public/callBack"));
        // 扫码事件处理
        log.error("推送链接 让用户授权");
        return TextBuilder.build("请点击登录 : <a href=\" " +authorizeUrl+"\">登录</a>",wxMpXmlMessage);


    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openid = userInfo.getOpenid();
        User user = userDao.getByOpenId(openid);
        //更新用户场景
        if(StrUtil.isBlank(user.getAvatar())){
            fillUserInfo(user.getId(),userInfo);
        }
        //通过code  找到用户Channel进行登陆
        Integer code = WAIT_AUTHORIZE_MAP.remove(openid);

        webSocketService.scanLoginSuccess(code,user.getId());


    }

    private void fillUserInfo(Long id, WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUser(id, userInfo);
        boolean update = userDao.updateById(user);
        log.error("更新用户头像:"+update);
    }

    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        try {
            String eventKey = wxMpXmlMessage.getEventKey();
            String code = eventKey.replace("qrscene_", "");
            return Integer.parseInt(code);
        } catch (Exception e) {
            log.error("getEvent error eventKey: {}", wxMpXmlMessage.getEventKey(), e);
            return null;
        }
    }
}
