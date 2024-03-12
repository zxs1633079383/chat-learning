package com.zlc.chat.chat.common.user.dao;

import com.zlc.chat.chat.common.user.domain.entity.Role;
import com.zlc.chat.chat.common.user.mapper.RoleMapper;
import com.zlc.chat.chat.common.user.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zxs1633079383">zlc</a>
 * @since 2024-03-12
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role>  {

}
