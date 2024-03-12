package com.zlc.chat.chat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--19:20
 * 3. 目的:
 */

@Data
public class WearingBlackReq {

    @ApiModelProperty("拉黑用户的ID")
    @NotNull
    private Long uid;


}
