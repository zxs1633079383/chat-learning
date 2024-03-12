package com.zlc.chat.chat.common.common.exception;

import cn.hutool.http.ContentType;
import com.zlc.chat.chat.common.common.domain.vo.resp.ApiResult;
import com.zlc.chat.chat.common.common.utils.JsonUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.io.Charsets;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--14:55
 * 3. 目的:
 */

@AllArgsConstructor
public enum HttpErrorEnum {
    ACCESS_DENIED(401, "登录失效请重新登录");
    private Integer httpCode;
    private String desc;

    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(httpCode);
        response.setContentType(ContentType.JSON.toString(Charsets.UTF_8));
        response.getWriter().write(JsonUtils.toStr(ApiResult.fail(httpCode, desc)));
    }
}
