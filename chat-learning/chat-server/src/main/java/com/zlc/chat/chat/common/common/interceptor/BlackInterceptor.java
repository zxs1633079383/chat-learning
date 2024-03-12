package com.zlc.chat.chat.common.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.zlc.chat.chat.common.common.constant.BlackTypeEnum;
import com.zlc.chat.chat.common.common.domain.dto.RequestInfo;
import com.zlc.chat.chat.common.common.exception.HttpErrorEnum;
import com.zlc.chat.chat.common.common.utils.RequestHolder;
import com.zlc.chat.chat.common.user.service.Cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/12--16:40
 * 3. 目的:
 */

@Component
public class BlackInterceptor implements HandlerInterceptor {

    @Autowired
    private UserCache userCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<Integer, Set<String>> map = userCache.getBlackMap();
        RequestInfo requestInfo = RequestHolder.get();
        if (inBlackList(requestInfo.getUid(),map.get(BlackTypeEnum.UID.getType()))) {
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return  false;
        }
        if (inBlackList(requestInfo.getIp(),map.get(BlackTypeEnum.IP.getType()))) {
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return  false;
        }
        return  true;

    }

    private boolean inBlackList(Object target, Set<String> set) {
        if(Objects.isNull(target) || CollectionUtil.isEmpty(set)){
            return  false;
        }

        return  set.contains(target.toString());
    }
}
