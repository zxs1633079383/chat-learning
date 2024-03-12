package com.zlc.chat.chat.common.user.dao;

import com.zlc.chat.chat.common.user.domain.entity.UserRole;
import com.zlc.chat.chat.common.user.mapper.UserRoleMapper;
import com.zlc.chat.chat.common.user.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zxs1633079383">zlc</a>
 * @since 2024-03-12
 */
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    public List<UserRole> listByUid(Long uid) {
        return lambdaQuery()
                .eq(UserRole::getUid,uid)
                .list();
    }
}
