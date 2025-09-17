package com.example.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	public static final Logger Logger=LoggerFactory.getLogger(LoggingAspect.class);
	
	@Around("execution(* com.example.demo.controller.BlogController.*(..))||"+
			"execution(* com.example.demo.service.BlogService.*(..))")
	public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable{
		String methodName=joinPoint.getSignature().toShortString();
		Logger.error("NITHYA -- Entering method: {}",methodName);
		long startTime=System.nanoTime();
		try {
			Object result=joinPoint.proceed();
			long endTime=System.nanoTime();
			long duration=(endTime-startTime)/1000000;
			Logger.info("NITHYA -- Exiting method: {}, Execution time: {}",methodName,duration);
			return result;
		}catch(Throwable throwable){
			Logger.error("Exception in method: {}, Error: {}",methodName,throwable.getMessage());
			throw throwable;
		}
	}
}

