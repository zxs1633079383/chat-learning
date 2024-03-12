package com.zlc.chat.chat.common.user.service;

import com.zlc.chat.chat.common.common.constant.IdempotentEnum;
import com.zlc.chat.chat.common.user.domain.entity.UserBackpack;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户背包表 服务类
 * </p>
 *
 * @author <a href="https://github.com/zxs1633079383">zlc</a>
 * @since 2024-03-10
 */
public interface IUserBackpackService {

    /***
     * 给用户发放一个物品
     * @param uid 用户ID
     * @param itemId 物品ID
     * @param idempotentEnum 幂等类型
     * @param businessId 幂等唯一标识
     */
    void accquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum,String businessId);


}
