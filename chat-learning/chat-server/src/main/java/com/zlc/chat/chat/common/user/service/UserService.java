package com.zlc.chat.chat.common.user.service;

import com.zlc.chat.chat.common.user.domain.entity.User;
import com.zlc.chat.chat.common.user.domain.vo.req.ModifyNameReq;
import com.zlc.chat.chat.common.user.domain.vo.req.WearingBlackReq;
import com.zlc.chat.chat.common.user.domain.vo.resp.BadgeResp;
import com.zlc.chat.chat.common.user.domain.vo.resp.UserInfoResp;

import java.util.List;

public interface UserService {
    /**
     * 注册
     * @param userSave
     * @return
     */
    Long register(User userSave);

    //获取用户详情
    UserInfoResp getUserInfo(Long uid);

    //修改名字
    void modifyName(Long uid, String req);

    /**
     * 可选徽章预览
     * @param uid
     * @return
     */
    List<BadgeResp> badges(Long uid);


    void wearingBadge(Long uid, Long itemId);

    /**
     * 拉黑用户
     * @param req
     */
    void black(WearingBlackReq req);
}
