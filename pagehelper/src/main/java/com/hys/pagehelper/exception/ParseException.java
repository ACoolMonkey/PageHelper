package com.hys.pagehelper.exception;

/**
 * 自定义解析异常
 *
 * @author Robert Hou
 * @since 2020年11月29日 13:14
 **/
public class ParseException extends RuntimeException {

    private static final long serialVersionUID = -6043973899043789981L;

    public ParseException() {
    }

    public ParseException(String message) {
        super(message);
    }
}
