package com.tytao.community.aspect;

import com.tytao.community.annotation.MetricTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MetricAspect {
    @Pointcut("execution(* com.tytao.community.service.*.*(..))")
    public void pointcut(){

    }

    @Around("pointcut()")
    public Object metric(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().getDeclaringType().getSimpleName()
                + ":" + joinPoint.getSignature().getName();
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long t = System.currentTimeMillis() - start;
            // 写入日志或发送至JMX:
            System.err.println("[Metrics] " + name + ": " + t + "ms");
        }
    }
}