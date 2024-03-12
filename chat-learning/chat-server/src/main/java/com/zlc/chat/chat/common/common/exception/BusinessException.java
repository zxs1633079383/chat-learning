package com.zlc.chat.chat.common.common.exception;

import lombok.Data;

/**
 * 1. 作者: 16330
 * 2. 日期: 2024/3/10--16:35
 * 3. 目的:
 */

@Data
public class BusinessException extends RuntimeException {

    protected Integer errorCode;

    protected String errorMsg;

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorCode = CommonErrorEnum.BUSINESS_ERROR.getErrorCode();
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(ErrorEnum lockLimit) {
        super(lockLimit.getErrorMsg());
        this.errorCode = lockLimit.getErrorCode();
        this.errorMsg = lockLimit.getErrorMsg();
    }

    @Override
    public String getMessage() {
        return this.errorMsg;
    }
}
