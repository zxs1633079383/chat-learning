package com.zlc.chat.chat.common.websocket.domain.vo.req;

import com.zlc.chat.chat.common.websocket.domain.enums.WSReqTypeEnum;
import lombok.Data;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/9--8:23
 * 3. 目的:
 */


@Data
public class WSBaseReq {

    /**
     * @see  WSReqTypeEnum
     */
    private Integer type;
    private String data;



}
