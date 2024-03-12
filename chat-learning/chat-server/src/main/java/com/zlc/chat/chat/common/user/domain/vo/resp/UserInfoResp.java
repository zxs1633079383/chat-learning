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
public class UserInfoResp {

    @ApiModelProperty(value = "uid")
    private Long id;
    @ApiModelProperty(value = "用户名称")
    private String name;
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    @ApiModelProperty(value = "用户性别")
    private Integer sex;
    @ApiModelProperty(value = "改名剩余次数")
    private Integer modifyNameChance;
}
