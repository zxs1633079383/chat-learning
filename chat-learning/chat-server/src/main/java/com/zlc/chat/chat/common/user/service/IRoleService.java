package com.zlc.chat.chat.common.user.service;

import com.zlc.chat.chat.common.common.constant.RoleEnum;
import com.zlc.chat.chat.common.user.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author <a href="https://github.com/zxs1633079383">zlc</a>
 * @since 2024-03-12
 */
public interface IRoleService {
    /***
     * 是否拥有某个权限 临时写法
     * @param uid
     * @param roleEnum
     * @return
     */
    boolean hasPower(Long uid , RoleEnum roleEnum);
}
