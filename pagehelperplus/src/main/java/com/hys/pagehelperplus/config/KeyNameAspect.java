package com.hys.pagehelperplus.config;

import com.hys.pagehelperplus.annotation.KeyNamesStrategy;
import com.hys.pagehelperplus.constant.KeyNamesStrategyEnum;
import com.hys.pagehelperplus.util.PageHelperUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 表主键名称切面
 *
 * @author Robert Hou
 * @since 2020年11月28日 23:20
 **/
@Aspect
@Component
public class KeyNameAspect {

    @Pointcut("@annotation(com.hys.pagehelperplus.annotation.KeyNamesStrategy)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        KeyNamesStrategy annotation = method.getAnnotation(KeyNamesStrategy.class);
        KeyNamesStrategyEnum strategy = annotation.strategy();
        String[] keyNames = annotation.keyNames();
        if (strategy == KeyNamesStrategyEnum.DEFAULT) {
            //@KeyNamesStrategy注解为默认配置，就将表主键名设置为”id“
            PageHelperUtils.setKeyNames(new String[]{"id"});
            return joinPoint.proceed();
        } else if (strategy == KeyNamesStrategyEnum.MUST_TELL && keyNames.length == 0) {
            //如果配置为自定义的表主键名，但是没有传进来其名称，则抛异常
            throw new IllegalArgumentException("@KeyNamesStrategy注解参数配置有误！keyNames配置项不能为空！");
        } else if (strategy == KeyNamesStrategyEnum.MUST_TELL) {
            //自定义的表主键名
            PageHelperUtils.setKeyNames(keyNames);
            return joinPoint.proceed();
        }
        throw new UnsupportedOperationException("没有分析到这种情况，需要进行排查！");
    }
}
