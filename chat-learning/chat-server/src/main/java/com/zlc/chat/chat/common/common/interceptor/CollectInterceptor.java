package com.zlc.chat.chat.common.common.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import com.zlc.chat.chat.common.common.domain.dto.RequestInfo;
import com.zlc.chat.chat.common.common.utils.RequestHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--15:26
 * 3. 目的:
 */

@Component
public class CollectInterceptor implements HandlerInterceptor {

    //收集数据
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long uid = Optional.ofNullable(request.getAttribute(TokenInterceptor.AUTHORIZATION_UID)).map(Object::toString).map(Long::parseLong).orElse(null);
        //ip
        String ip = ServletUtil.getClientIP(request);
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setIp(ip);
        requestInfo.setUid(uid);
        RequestHolder.set(requestInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
    }
}
