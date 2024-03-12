package com.zlc.chat.chat.common.user.service.Cache;

import com.zlc.chat.chat.common.user.dao.ItemConfigDao;
import com.zlc.chat.chat.common.user.domain.entity.ItemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--17:31
 * 3. 目的:
 */

@Component
public class ItemCache {

    @Autowired
    private ItemConfigDao itemConfigDao;

    @Cacheable(cacheNames = "item",key = "'itemsByType:'+#itemType")
    public List<ItemConfig> getByType(Integer itemType){
        return itemConfigDao.getValidByType(itemType);
    }


    @CacheEvict(cacheNames = "item",key = "'itemsByType:'+#itemType")
    public void evitByType(Integer itemType){

    }


}
