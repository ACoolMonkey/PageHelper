package com.hys.pagehelperplus.demo.constant;

/**
 * 状态码
 *
 * @author Robert Hou
 * @since 2020年11月28日 14:00
 **/
public enum StatusCodeEnum {

    SUCCESS("0000", "成功"),

    PARAM_TYPE_ERROR("1000", "参数类型错误"),

    USER_NOT_LOGIN("2001", "用户未登录"),

    USER_NOT_EXIST("2003", "用户不存在"),

    SERVER_ERROR("4000", "服务端错误");

    private final String code;
    private final String msg;

    StatusCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
