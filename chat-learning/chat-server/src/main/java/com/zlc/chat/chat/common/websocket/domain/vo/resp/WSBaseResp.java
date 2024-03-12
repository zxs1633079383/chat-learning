package com.zlc.chat.chat.common.websocket.domain.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--8:26
 * 3. 目的:
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WSBaseResp<T> {

    /**
     * @see com.zlc.chat.chat.common.websocket.domain.enums.WSRespTypeEnum
     */
    private Integer type;
    private  T data;
}
