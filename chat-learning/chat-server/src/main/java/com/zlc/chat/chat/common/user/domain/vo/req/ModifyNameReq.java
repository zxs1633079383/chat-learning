package com.zlc.chat.chat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--15:55
 * 3. 目的:
 */

@Data
public class ModifyNameReq {

    @ApiModelProperty("用户名")
    @NotBlank
    @Length(max = 6,message = "用户名不可以取太长, 不然我们记不住哦")
    private String name;

    @NotNull(message = "别忘记传ID哦")
    private Integer id;

}
