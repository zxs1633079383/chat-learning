package com.zlc.chat.chat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zlc.chat.chat.common.common.constant.RedisKey;
import com.zlc.chat.chat.common.common.utils.JwtUtils;
import com.zlc.chat.chat.common.common.utils.RedisUtils;
import com.zlc.chat.chat.common.user.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--16:31
 * 3. 目的:
 */

@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private JwtUtils jwtUtils;

    public static final int TOKEN_EXPIRE = 3;


    // 异步刷新 延长Token的过期时间
    @Override
    @Async
    public void renewalTokenIfNecessary(String token) {


        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        Long expireDays = RedisUtils.getExpire(userTokenKey, TimeUnit.DAYS);
        if(expireDays == -1){ //不存在的key
            return;
        }
        if(expireDays < 1){
            RedisUtils.expire(getUserTokenKey(uid),TOKEN_EXPIRE,TimeUnit.DAYS);
        }
    }

    @Override
    public String login(Long id) {
        //返回token
        String token = jwtUtils.createToken(id);
        RedisUtils.set(getUserTokenKey(id),token,TOKEN_EXPIRE, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {

        Long uid = jwtUtils.getUidOrNull(token);
        if(Objects.isNull(uid)){
            return null;
        }
        String oldToken = RedisUtils.getStr(getUserTokenKey(uid));
        if(StringUtils.isEmpty(oldToken)){
            return null;
        }

        return Objects.equals(oldToken,token) ? uid : null;

    }

    private String getUserTokenKey(Long uid){
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING,uid);
    }
}
