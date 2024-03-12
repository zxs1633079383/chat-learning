package com.zlc.chat.chat.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zlc.chat.chat.common.common.config.ThreadPoolConfig;
import com.zlc.chat.chat.common.common.constant.RoleEnum;
import com.zlc.chat.chat.common.common.event.UserOnlineEvent;
import com.zlc.chat.chat.common.user.dao.UserDao;
import com.zlc.chat.chat.common.user.domain.entity.User;
import com.zlc.chat.chat.common.user.service.IRoleService;
import com.zlc.chat.chat.common.user.service.LoginService;
import com.zlc.chat.chat.common.websocket.NettyUtils;
import com.zlc.chat.chat.common.websocket.domain.dto.WSChannelExtraDTO;
import com.zlc.chat.chat.common.websocket.domain.vo.resp.WSBaseResp;
import com.zlc.chat.chat.common.websocket.service.WebSocketService;
import com.zlc.chat.chat.common.websocket.service.adapter.WebSocketAdapter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--14:26
 * 3. 目的:
 */

@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserDao userDao;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    @Qualifier(ThreadPoolConfig.WS_EXECUTOR)
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 管理所有用户的连接(登录态/游客)
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    /**
     * 本地缓存 临时保存code和channle的映射关系.
     */
    private static final Duration DURATION = Duration.ofHours(1);
    /**
     * 最大容量
     */
    public static final int MAXIMUM_SIZE = 10000;

    private static final Cache<Integer, Channel> WAITE_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(DURATION)
            .build();


    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }

    @Override
    public void handleLoginReq(Channel channel) {
        //生成随机码
        Integer code = generateLoginCode(channel);
        //找微信申请带参二维码
        try {
            WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
            sendMsg(channel, WebSocketAdapter.buildResp(wxMpQrCodeTicket));
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }

        //二维码推送给前端
    }

    @Override
    public void offLine(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
        //todo 用户下线
    }

    @Override
    public void scanLoginSuccess(Integer code, Long id) {
        //确认链接在机器上
        Channel channel = WAITE_LOGIN_MAP.getIfPresent(code);
        if(Objects.isNull(channel)){
            return;
        }
        User user = userDao.getById(id);
        //移除Code
        WAITE_LOGIN_MAP.invalidate(code);
        //调用登录模块获取token
        String token = loginService.login(id);
        //用户登录
        sendMsg(channel,WebSocketAdapter.buildResp(user,token, iRoleService.hasPower(user.getId(), RoleEnum.CHAT_MANAGER)));
        loginSuccess(channel,user, token);

    }

    @Override
    public void waiteAuthorize(Integer code) {
        Channel channel = WAITE_LOGIN_MAP.getIfPresent(code);
        if(Objects.isNull(channel)){
            return;
        }
        sendMsg(channel,WebSocketAdapter.buildWaitAuthorizeResp());
    }

    @Override
    public void authorize(Channel channel, String token) {
        Long validUid = loginService.getValidUid(token);
        if(Objects.nonNull(validUid)){
            //获取用户
            User user = userDao.getById(validUid);
            loginSuccess(channel,user,token);
        }else{
            sendMsg(channel,WebSocketAdapter.buildInValidTokenResp());
        }
    }

    @Override
    public void sendMsgToAll(WSBaseResp<?> msg) {

            ONLINE_WS_MAP.forEach((channel,text)->{
                executor.execute(()->{
                    sendMsg(channel,msg);
                });
            });

    }

    //用户登录成功推送
    private void loginSuccess(Channel channel, User user, String token)
    {
        //保存channel对应的channleid
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());

        //推送成功消息
        sendMsg(channel,WebSocketAdapter.buildResp(user,token,iRoleService.hasPower(user.getId(), RoleEnum.CHAT_MANAGER)));

        //todo 用户上线成功的事件
        user.setLastOptTime(new Date());
        user.refreshIp(NettyUtils.getAttr(channel,NettyUtils.IP));
        log.error("用户上线成功");
        applicationEventPublisher.publishEvent(new UserOnlineEvent(this,user));
    }

    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (Objects.nonNull(WAITE_LOGIN_MAP.asMap().putIfAbsent(code, channel)));

        return  code;
    }
}
