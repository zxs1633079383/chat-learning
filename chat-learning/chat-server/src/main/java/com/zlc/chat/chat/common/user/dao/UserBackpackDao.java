package com.zlc.chat.chat.common.user.dao;

import com.zlc.chat.chat.common.common.constant.ItemEnum;
import com.zlc.chat.chat.common.common.constant.YesOrNoEnum;
import com.zlc.chat.chat.common.user.domain.entity.UserBackpack;
import com.zlc.chat.chat.common.user.mapper.UserBackpackMapper;
import com.zlc.chat.chat.common.user.service.IUserBackpackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zxs1633079383">zlc</a>
 * @since 2024-03-10
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack> {

    public Integer getCountByValidItemId(Long uid, Long itemId) {
        Integer count = lambdaQuery().eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .count();
        return count;
    }

    /**
     * 获取第一张可用的卡
     *
     * @param uid
     * @param id
     */
    public UserBackpack getFirstVaildItem(Long uid, Long id) {
        return lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, id)
                .eq(UserBackpack::getStatus,0)
                .orderByAsc(UserBackpack::getId)
                .last("limit 1")
                .one();
    }

    public boolean useItem(UserBackpack modifyNameItem) {
        return lambdaUpdate()
                .eq(UserBackpack::getId,modifyNameItem.getId())
                .eq(UserBackpack::getStatus,YesOrNoEnum.NO.getStatus())
                .set(UserBackpack::getStatus,YesOrNoEnum.YES.getStatus()).update();
    }

    public List<UserBackpack> getByItemIds(Long uid, List<Long> collect) {
        return lambdaQuery()
                .eq(UserBackpack::getUid,uid)
                .eq(UserBackpack::getStatus,YesOrNoEnum.NO.getStatus())
                .in(UserBackpack::getItemId,collect)
                .list();
    }

    public UserBackpack getByIdmpotent(String idempotent) {
        return lambdaQuery()
                .eq(UserBackpack::getIdempotent, idempotent)
                .one();
    }
}
