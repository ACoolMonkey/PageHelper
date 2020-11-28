package com.hys.pagehelperplus.constant;

/**
 * 表主键名称策略枚举
 *
 * @author Robert Hou
 * @since 2020年11月27日 19:15
 **/
public enum KeyNamesStrategyEnum {

    /**
     * 默认策略，默许表的主键名为“id”，以这种方式进行分页SQL的拼装
     */
    DEFAULT,
    /**
     * 如果表的主键名不为“id”，则使用这种策略，同时必须传进来自定义的表主键名
     */
    MUST_TELL;
}
