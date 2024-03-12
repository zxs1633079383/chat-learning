package com.zlc.chat.chat.common.user.mapper;

import com.zlc.chat.chat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author <a href="https://github.com/zxs1633079383">zlc</a>
 * @since 2024-03-09
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
