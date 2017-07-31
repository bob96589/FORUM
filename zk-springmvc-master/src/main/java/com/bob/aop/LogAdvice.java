package com.bob.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogAdvice {

	private final static Logger logger = LoggerFactory.getLogger(LogAdvice.class);

	public Object logAroundExecute(ProceedingJoinPoint pjp) throws Throwable {
		logger.debug("Start: {}.{}", pjp.getTarget().getClass(), pjp.getSignature().getName());
		Object obj = pjp.proceed();
		return obj;
	}

	public void logAfterExecute(JoinPoint joinPoint, Object reVal) {
		logger.info("End: {}.{}", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
	}

	public void logAfterThrowingException(JoinPoint joinPoint, Exception exception) {
		logger.info("Exception: {}.{} {}", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(),
				exception);
	}

}
