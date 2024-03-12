package com.zlc.chat.chat.common.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/11--11:54
 * 3. 目的:
 */

@Getter
@AllArgsConstructor
public enum UserActiveStatusEnum {

    ONLINE(1, "在线"),
    OFFLINE(2, "离线");

    private final Integer status;
    private final String desc;

    }
