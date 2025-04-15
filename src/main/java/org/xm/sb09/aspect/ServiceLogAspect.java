package org.xm.sb09.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ServiceLogAspect {
    @Pointcut("execution( * org.xm.sb09.services..*(..))")
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint jp) {
        long startTimeMs = System.currentTimeMillis();
        try {
            return jp.proceed();
        } catch (Throwable thr) {
            throw new RuntimeException(thr);
        } finally {
            long endTimeMs = System.currentTimeMillis();
            log.info("The method" +  jp.getSignature().toLongString() + "'s execution time is: " + (endTimeMs - startTimeMs));
        }
    }
}
