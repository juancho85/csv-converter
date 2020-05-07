package com.juancho85.statistics;

import lombok.extern.log4j.Log4j2;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Log4j2
public class MonitorAspect implements MethodInterceptor {

    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = invocation.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("{} executed in {} ms", invocation.getMethod(), executionTime);
        return result;
    }
}
