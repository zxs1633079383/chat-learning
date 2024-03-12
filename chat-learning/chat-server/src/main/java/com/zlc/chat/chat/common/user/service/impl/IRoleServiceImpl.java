package com.zlc.chat.chat.common.user.service.impl;

import com.zlc.chat.chat.common.common.constant.RoleEnum;
import com.zlc.chat.chat.common.user.service.Cache.UserCache;
import com.zlc.chat.chat.common.user.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/12--14:12
 * 3. 目的:
 */

@Service
public class IRoleServiceImpl implements IRoleService {

    @Autowired
    private UserCache userCache;

    @Override
    public boolean hasPower(Long uid, RoleEnum roleEnum) {
        //获取
        Set<Long> roleSet = userCache.getRoleSet(uid);
        return roleSet.contains(roleEnum.getId());
    }

    private boolean isAdmin(Set<Long> roleSet){
        return roleSet.contains(RoleEnum.ADMIN.getId());
    }
}
