package com.hys.pagehelperplus.annotation;

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
     * 如果表主键名称不为“id”，又或者有多个联合主键，就需要将自定义的表主键名称传到这里来
     */
    String[] keyNames() default {};
}
