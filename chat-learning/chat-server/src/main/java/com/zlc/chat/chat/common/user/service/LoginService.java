package com.zlc.chat.chat.common.user.service;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--16:31
 * 3. 目的:
 */

public interface LoginService {

    /**
     * 刷新token有效期
     *
     * @param token
     */
    void renewalTokenIfNecessary(String token);

    /**
     * 登录成功，获取token
     *
     * @param uid
     * @return 返回token
     */
    String login(Long uid);

    /**
     * 如果token有效，返回uid
     *
     * @param token
     * @return
     */
    Long getValidUid(String token);
}
