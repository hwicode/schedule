package hwicode.schedule.common.timetrace;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class TimeTraceAop {

    @Around("execution(* hwicode..*Controller.*(..)) " +
            "or execution(* hwicode..*Service.*(..)) " +
            "or execution(* hwicode..jpa_repository.*.*(..))")
    public Object logTimeTrace(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();

        try {
            return proceedingJoinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            log.info("method name: {}, spent time: {}ms", methodName, timeMs);
        }
    }

}
