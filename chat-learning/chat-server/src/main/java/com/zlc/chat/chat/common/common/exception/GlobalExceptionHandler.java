package com.zlc.chat.chat.common.common.exception;

import com.zlc.chat.chat.common.common.domain.vo.resp.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--16:05
 * 3. 目的:
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<?> methdArgumentNotVaildException(MethodArgumentNotValidException e) {
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach(x -> errorMsg.append(x.getField()).append(" " + x.getDefaultMessage()).append(","));
        String message = errorMsg.toString();

        System.out.println(message.substring(0, message.length() - 1));
        return ApiResult.fail(CommonErrorEnum.PARAM_INVALID.getCode(), message.substring(0, message.length() - 1));
    }

    //兜底异常拦截
    @ExceptionHandler(value = Throwable.class)
    public ApiResult<?> throwable(Throwable e) {
        log.error("system exception! The reason is: {}", e.getMessage());
        return ApiResult.fail(CommonErrorEnum.SYSTEM_ERROR);
    }

    //自定义业务异常
    @ExceptionHandler(value = BusinessException.class)
    public ApiResult<?> businessException(BusinessException e) {
        log.error("business exception! The reason is: {}", e.getMessage(),e);
        return ApiResult.fail(e.getErrorCode(), e.getErrorMsg());
    }

}
