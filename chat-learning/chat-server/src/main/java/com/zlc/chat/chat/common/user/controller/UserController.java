package com.zlc.chat.chat.common.user.controller;


import com.zlc.chat.chat.common.common.constant.RoleEnum;
import com.zlc.chat.chat.common.common.domain.vo.resp.ApiResult;
import com.zlc.chat.chat.common.common.interceptor.TokenInterceptor;
import com.zlc.chat.chat.common.common.utils.AssertUtil;
import com.zlc.chat.chat.common.common.utils.RequestHolder;
import com.zlc.chat.chat.common.user.domain.vo.req.ModifyNameReq;
import com.zlc.chat.chat.common.user.domain.vo.req.WearingBadgeReq;
import com.zlc.chat.chat.common.user.domain.vo.req.WearingBlackReq;
import com.zlc.chat.chat.common.user.domain.vo.resp.BadgeResp;
import com.zlc.chat.chat.common.user.domain.vo.resp.UserInfoResp;
import com.zlc.chat.chat.common.user.service.IRoleService;
import com.zlc.chat.chat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/zxs1633079383">zlc</a>
 * @since 2024-03-09
 */
@RestController
@RequestMapping("/capi/user")
@Api(value = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IRoleService iRoleService;

    @GetMapping("/public/userInfo")
    @ApiOperation("获取用户个人信息")
    public ApiResult<UserInfoResp> getUserInfo(HttpServletRequest request) {

        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    @PutMapping("/name")
    @ApiOperation("修改用户名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq req) {
        userService.modifyName(RequestHolder.get().getUid(), req.getName());
        return null;
    }

    @GetMapping("/badges")
    @ApiOperation("可选徽章预览")
    public ApiResult<List<BadgeResp>> badges() {
        return ApiResult.success(userService.badges(RequestHolder.get().getUid()));
    }

    @PutMapping("/badge")
    @ApiOperation("佩戴徽章")
    public ApiResult<Void> wearingBadge(@Valid @RequestBody WearingBadgeReq req) {
        userService.wearingBadge(RequestHolder.get().getUid(), req.getItemId());
        return ApiResult.success();
    }

    @PutMapping("/black")
    @ApiOperation("拉黑用户")
    public ApiResult<Void> black(@Valid @RequestBody WearingBlackReq req) {
        //当前用户拉黑执行的拉黑操作.  判断她的权限
        Long uid = RequestHolder.get().getUid();
        boolean power = iRoleService.hasPower(uid, RoleEnum.ADMIN);
        AssertUtil.isTrue(power,"管理系统无权限");
        userService.black(req);
        return ApiResult.success();
    }

}

