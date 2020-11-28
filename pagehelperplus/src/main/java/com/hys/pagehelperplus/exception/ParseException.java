package com.hys.pagehelperplus.exception;

/**
 * 自定义解析异常
 *
 * @author Robert Hou
 * @date 2020年11月27日 21:56
 **/
public class ParseException extends RuntimeException {

    public ParseException() {
    }

    public ParseException(String message) {
        super(message);
    }
}
