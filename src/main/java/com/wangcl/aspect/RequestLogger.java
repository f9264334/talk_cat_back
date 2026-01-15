package com.wangcl.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 功能描述：
 *
 * @author wangchenglong
 * @version 1.0
 * @date 2026/1/15 17:05
 */
//@Aspect
//@ControllerAdvice
public class RequestLogger {
    @Pointcut("execution(public * com.wangcl.controller.*.*(..))")
    public void log() {

    }
}
