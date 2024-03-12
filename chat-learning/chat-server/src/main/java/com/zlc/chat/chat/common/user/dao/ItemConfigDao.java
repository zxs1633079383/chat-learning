package com.zlc.chat.chat.common.user.dao;

import com.zlc.chat.chat.common.user.domain.entity.ItemConfig;
import com.zlc.chat.chat.common.user.mapper.ItemConfigMapper;
import com.zlc.chat.chat.common.user.service.IItemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 功能物品配置表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zxs1633079383">zlc</a>
 * @since 2024-03-10
 */
@Service
public class ItemConfigDao extends ServiceImpl<ItemConfigMapper, ItemConfig> {

    public List<ItemConfig> getValidByType(Integer itemType) {
        return lambdaQuery()
                .eq(ItemConfig::getType,itemType)
                .list();
    }
}
