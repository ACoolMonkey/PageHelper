package com.hys.pagehelper.aop;

import com.hys.pagehelper.aop.annotation.KeyNamesStrategy;
import com.hys.pagehelper.util.PageHelperUtils;
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
public class KeyNamesAspect {

    @Pointcut("@annotation(com.hys.pagehelper.aop.annotation.KeyNamesStrategy)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        KeyNamesStrategy annotation = method.getAnnotation(KeyNamesStrategy.class);
        String[] keyNames = annotation.keyNames();
        boolean relegate = annotation.relegate();
        if (relegate) {
            //如果降级，则直接走调用方法
            PageHelperUtils.setIsRelegated(true);
            return joinPoint.proceed();
        }
        PageHelperUtils.setIsRelegated(false);

        if (keyNames.length == 0) {
            //@KeyNamesStrategy注解为默认配置，就将表主键名设置为“id”
            PageHelperUtils.setKeyNames(new String[]{"id"});
        } else {
            //自定义的表主键名
            PageHelperUtils.setKeyNames(keyNames);
        }
        return joinPoint.proceed();
    }
}
