package com.nowcoder.community.controller.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut() {

    }

    /**
     * 连接点开始之前
     */
    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    /**
     * 连接点之后
     */
    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    /**
     * 返回值之后
     */
    @AfterReturning("pointcut()")
    public void afterReturning() {
        System.out.println("afterReturning");
    }

    /**
     * 抛异常之后
     */
    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    /**
     * 想在之前和之后都织入
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        //目标主键的返回值
        Object proceed = joinPoint.proceed();
        System.out.println("around after");
        return proceed;
    }
}
