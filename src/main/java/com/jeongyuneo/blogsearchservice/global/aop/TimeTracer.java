package com.jeongyuneo.blogsearchservice.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeTracer {

    @Around("execution(* com.jeongyuneo.blogsearchservice..*Controller.*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            log.info("{}: executed {}ms", joinPoint, System.currentTimeMillis() - start);
        }
    }
}
