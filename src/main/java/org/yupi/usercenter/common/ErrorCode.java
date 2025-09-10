package org.yupi.usercenter.common;

public enum ErrorCode {
    // 有参枚举类，后面是下面的构造函数
    UNSPECIFIED(500, "网络异常，请稍后再试", ""),
    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数出错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTO(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述
     */
    private final String description;


    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    // 用来获取枚举对象中的属性
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }



}
