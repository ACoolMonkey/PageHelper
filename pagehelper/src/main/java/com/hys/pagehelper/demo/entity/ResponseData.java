package com.hys.pagehelper.demo.entity;

import com.hys.pagehelper.demo.constant.StatusCodeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应数据封装
 *
 * @author Robert Hou
 * @since 2020年11月28日 14:00
 **/
@Data
@NoArgsConstructor
public class ResponseData {

    private String code;
    private String msg;
    private Object data;

    private ResponseData(StatusCodeEnum codeEnum, Object data) {
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg();
        this.data = data;
    }

    public static ResponseData success() {
        return new ResponseData(StatusCodeEnum.SUCCESS, null);
    }

    public static ResponseData success(Object data) {
        return new ResponseData(StatusCodeEnum.SUCCESS, data);
    }

    public static ResponseData fail(StatusCodeEnum codeEnum) {
        return new ResponseData(codeEnum, null);
    }

    public static ResponseData fail(StatusCodeEnum codeEnum, Object data) {
        return new ResponseData(codeEnum, data);
    }
}
