package com.zlc.chat.chat.common.user.service.Cache;

import com.zlc.chat.chat.common.user.dao.BlackDao;
import com.zlc.chat.chat.common.user.dao.ItemConfigDao;
import com.zlc.chat.chat.common.user.dao.UserRoleDao;
import com.zlc.chat.chat.common.user.domain.entity.Black;
import com.zlc.chat.chat.common.user.domain.entity.ItemConfig;
import com.zlc.chat.chat.common.user.domain.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--17:31
 * 3. 目的:
 */

@Component
public class UserCache {

    @Autowired
    private ItemConfigDao itemConfigDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private BlackDao blackDao;




    @Cacheable(cacheNames = "user",key = "'roles:'+#uid")
    public Set<Long> getRoleSet(Long uid) {
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        return userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toSet());
    }

    @Cacheable(cacheNames = "user",key = "'blackList'")
    public Map<Integer,Set<String>> getBlackMap() {
        Map<Integer, List<Black>> map = blackDao.list().stream().collect(Collectors.groupingBy(Black::getType));
        Map<Integer,Set<String>> result = new HashMap<>();
        map.forEach((type,list)->{
            result.put(type,list.stream().map(Black::getTarget).collect(Collectors.toSet()));
        });

        return result;

    }

    @CacheEvict(cacheNames = "user",key = "'blackList'")
    public Map<Integer,Set<String>> evictBlackMap(){


        return  null;
    }
}
