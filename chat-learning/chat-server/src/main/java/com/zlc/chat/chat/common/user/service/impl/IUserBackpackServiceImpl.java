package com.zlc.chat.chat.common.user.service.impl;

import com.zlc.chat.chat.common.common.annotation.RedissonLock;
import com.zlc.chat.chat.common.common.constant.IdempotentEnum;
import com.zlc.chat.chat.common.common.constant.YesOrNoEnum;
import com.zlc.chat.chat.common.common.service.LockService;
import com.zlc.chat.chat.common.common.utils.AssertUtil;
import com.zlc.chat.chat.common.user.dao.UserBackpackDao;
import com.zlc.chat.chat.common.user.domain.entity.UserBackpack;
import com.zlc.chat.chat.common.user.service.IUserBackpackService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/11--8:16
 * 3. 目的:
 */

@Service
public class IUserBackpackServiceImpl implements IUserBackpackService {

    @Autowired
    private LockService lockService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private UserBackpackDao userBackpackDao;

    @Autowired
    @Lazy

    private IUserBackpackServiceImpl userBackpackService;

    @Override
    public void accquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId);
        userBackpackService.doAcquireImte(uid, itemId, idempotent);
    }

    @RedissonLock(key = "#idempotent",waitTime = 5000)
    public void doAcquireImte(Long uid, Long itemID, String idempotent) {

        UserBackpack userBackpack = userBackpackDao.getByIdmpotent(idempotent);
        if (Objects.nonNull(userBackpack)) {
            return;
        }
        //发放物品
        UserBackpack insert = UserBackpack.builder()
                .uid(uid)
                .itemId(itemID)
                .status(YesOrNoEnum.NO.getStatus())
                .idempotent(idempotent)
                .build();
        userBackpackDao.save(insert);

    }

    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
    }


}
