package com.hys.pagehelper.aop.annotation;

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

    /**
     * 是否降级选项：true：降级；false：不降级（默认值）
     * 暂时提供的一个选项，当重写的分页逻辑出错而无法修改时，将该选项值置为true，将会走默认的PageHelper逻辑，而不是重写的自定义分页逻辑
     */
    boolean relegate() default false;
}
