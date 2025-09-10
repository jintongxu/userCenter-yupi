package org.yupi.usercenter.exception;

import lombok.Data;
import org.yupi.usercenter.common.ErrorCode;

@Data
public class BusinessException extends RuntimeException{


    /**
     * 异常码
     */
    private final int code;

    /**
     * 描述
     */
    private final String description;

    // 自定义异常 错误码 描述的异常
    public BusinessException(String message,int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    // 通用异常
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    // 自定义描述的异常
    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }



}
