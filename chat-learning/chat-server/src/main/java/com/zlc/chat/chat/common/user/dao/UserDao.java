package com.zlc.chat.chat.common.user.dao;

import com.zlc.chat.chat.common.common.constant.YesOrNoEnum;
import com.zlc.chat.chat.common.user.domain.entity.User;
import com.zlc.chat.chat.common.user.mapper.UserMapper;
import com.zlc.chat.chat.common.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zxs1633079383">zlc</a>
 * @since 2024-03-09
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {

    public User getByOpenId(String openId) {
        return lambdaQuery()
                .eq(User::getOpenId, openId)
                .one();
    }

    public User getByName(String req) {
        return lambdaQuery()
                .eq(User::getName, req)
                .one();
    }

    public boolean modifyName(Long uid, String req) {
        return lambdaUpdate()
                .eq(User::getId, uid)
                .set(User::getName, req)
                .update();
    }

    public void wearingBadge(Long uid, Long itemId) {
        lambdaUpdate()
                .eq(User::getId, uid)
                .set(User::getItemId, itemId).update();
    }

    /**
     * 修改拉黑用户的状态
     * @param id
     */
    public void invalidUid(Long id) {
        lambdaUpdate()
                .eq(User::getId,id)
                .set(User::getStatus, YesOrNoEnum.YES.getStatus())
                .update();
    }
}
