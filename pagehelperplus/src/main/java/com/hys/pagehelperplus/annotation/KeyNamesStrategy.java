package com.hys.pagehelperplus.annotation;

import com.hys.pagehelperplus.constant.KeyNamesStrategyEnum;

import java.lang.annotation.*;

/**
 * 表主键名称策略
 *
 * @author Robert Hou
 * @since 2020年11月27日 19:08
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KeyNamesStrategy {

    /**
     * 表主键名称策略
     *
     * @see KeyNamesStrategyEnum
     */
    KeyNamesStrategyEnum strategy() default KeyNamesStrategyEnum.DEFAULT;

    /**
     * 如果表主键名称的策略为MUST_TELL，则需要将自定义的表主键名称传到这里来（不传会抛异常）
     * 如果表主键名称的策略为DEFAULT，则会忽略掉本配置项的内容
     */
    String[] keyNames() default {};
}
