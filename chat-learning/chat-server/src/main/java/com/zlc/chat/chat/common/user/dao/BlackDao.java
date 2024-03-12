package com.zlc.chat.chat.common.user.dao;

import com.zlc.chat.chat.common.user.domain.entity.Black;
import com.zlc.chat.chat.common.user.mapper.BlackMapper;
import com.zlc.chat.chat.common.user.service.IBlackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 黑名单 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zxs1633079383">zlc</a>
 * @since 2024-03-12
 */
@Service
public class BlackDao extends ServiceImpl<BlackMapper, Black> implements IBlackService {

}
