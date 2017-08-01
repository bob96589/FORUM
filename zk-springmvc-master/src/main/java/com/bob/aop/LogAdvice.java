package com.bob.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogAdvice {

	private final Logger logger = LoggerFactory.getLogger(LogAdvice.class);

	public Object logAroundExecute(ProceedingJoinPoint pjp) throws Throwable {
		if (logger.isDebugEnabled()) {
			logger.debug("{}.{}(start)", pjp.getTarget().getClass(), pjp.getSignature().getName());
		}
		Object obj = pjp.proceed();
		return obj;
	}

	public void logAfterExecute(JoinPoint joinPoint, Object reVal) {
		if (logger.isDebugEnabled()) {
			logger.debug("{}.{}(end)", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
		}
	}

	public void logAfterThrowingException(JoinPoint joinPoint, Exception exception) {
		logger.error("{}.{}(Exception: {})", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(),
				exception);
	}

}
