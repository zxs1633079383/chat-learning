package com.zlc.chat.chat.common.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.zlc.chat.chat.common.common.constant.YesOrNoEnum;
import com.zlc.chat.chat.common.user.domain.entity.ItemConfig;
import com.zlc.chat.chat.common.user.domain.entity.User;
import com.zlc.chat.chat.common.user.domain.entity.UserBackpack;
import com.zlc.chat.chat.common.user.domain.vo.resp.BadgeResp;
import com.zlc.chat.chat.common.user.domain.vo.resp.UserInfoResp;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--15:30
 * 3. 目的:
 */

public class UserAdapter {
    public static User buildUserSave(String openId) {
        return User.builder()
                .openId(openId).build();
    }

    public static User buildAuthorizeUser(Long id, WxOAuth2UserInfo userInfo) {
        User user = new User();
        user.setId(id);
        user.setName(userInfo.getNickname());
        user.setAvatar(userInfo.getHeadImgUrl());
        return  user;



    }

    public static UserInfoResp buidUserInfo(User user, Integer count) {
        UserInfoResp vo  = new UserInfoResp();
        BeanUtil.copyProperties(user,vo);
        vo.setModifyNameChance(count);

        return  vo;
    }

    public static List<BadgeResp> buildBadegResp(List<ItemConfig> itemConfigs, List<UserBackpack> backpacks, User user) {
        Set<Long> obtainItemSet = backpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
        return itemConfigs.stream().map(a -> {
            BadgeResp resp = new BadgeResp();
            BeanUtil.copyProperties(a, resp);
            resp.setObtain(obtainItemSet.contains(a.getId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
            resp.setWearing(Objects.equals(a.getId(), user.getItemId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
            return resp;

        }).sorted(Comparator.comparing(BadgeResp::getWearing, Comparator.reverseOrder())
                .thenComparing(BadgeResp::getObtain, Comparator.reverseOrder())).collect(Collectors.toList());

    }
}
