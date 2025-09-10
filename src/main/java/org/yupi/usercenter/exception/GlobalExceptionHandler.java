package org.yupi.usercenter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yupi.usercenter.common.BaseResponse;
import org.yupi.usercenter.common.ErrorCode;
import org.yupi.usercenter.common.ResultUtils;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 任意控制器抛出 BusinessException 时，会进入这个方法。
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessExceptionHandler" + e.getMessage(), e);
        /**
         * 构造一个统一格式的错误响应（BaseResponse<?>），通常包含：
         * code：业务错误码（来自 e.getCode()）
         * message：面向用户/前端的错误提示（e.getMessage()）
         * description：更详细的描述（e.getDescription()）
         * data：错误时一般为 null 或不返回
         * 效果：比如抛出 throw new BusinessException(1001, "用户名已存在", "注册时检测到重复用户名");
         * 前端会拿到一个标准化的 JSON，便于统一处理 UI 提示。
         */
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     *当控制器或服务抛出 任意 RuntimeException（例如 NullPointerException、IllegalStateException 等）
     * 且没有被其他更具体的处理器捕获时，Spring 会调用此方法处理异常。
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
