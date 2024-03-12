package com.zlc.chat.chat.common.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/11--8:14
 * 3. 目的:
 */


@AllArgsConstructor
@Getter
public enum IdempotentEnum {

    UID(1,"uid"),
    MSG_ID(2,"消息id");

    private final Integer type;
    private final String desc;

}
