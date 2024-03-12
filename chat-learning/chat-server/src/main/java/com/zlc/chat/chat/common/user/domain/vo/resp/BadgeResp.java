package com.zlc.chat.chat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--10:55
 * 3. 目的:
 */

@Data
@ApiModel("用户返回类")
public class BadgeResp {

    @ApiModelProperty(value = "徽章")
    private Long id;
    @ApiModelProperty(value = "徽章图标")
    private String img;
    @ApiModelProperty(value = "徽章描述")
    private String describe;
    @ApiModelProperty(value = "是否拥有 0否 1是")
    private Integer obtain;
    @ApiModelProperty(value = "是否佩戴 0否 1是")
    private Integer wearing;
}
