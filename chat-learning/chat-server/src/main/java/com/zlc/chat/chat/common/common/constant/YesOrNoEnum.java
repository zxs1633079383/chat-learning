package com.zlc.chat.chat.common.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--15:46
 * 3. 目的:
 */

@AllArgsConstructor
@Getter
public enum YesOrNoEnum {

    NO(0,"否"),
    YES(1,"是");

    private final Integer status;
    private final String desc;


}
