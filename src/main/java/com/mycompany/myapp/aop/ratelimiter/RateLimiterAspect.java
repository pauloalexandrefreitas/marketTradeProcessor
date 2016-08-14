package com.mycompany.myapp.aop.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.RateLimiter;
import com.mycompany.myapp.config.annotations.RateLimit;
import com.mycompany.myapp.web.rest.errors.RateLimitException;

/**
 * Aspect for logging execution of service and repository Spring components.
 */
@Aspect
public class RateLimiterAspect {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final ConcurrentHashMap<String, RateLimiter> limiters = new ConcurrentHashMap<>();

	@Before("@annotation(rateLimit)")
	public void rateLimit(JoinPoint jp, RateLimit rateLimit) {
		RateLimiter l = limiters.computeIfAbsent(createKey(jp), createLimit(rateLimit));

		boolean b = l.tryAcquire(0, TimeUnit.SECONDS);

		if (b) {
			log.debug("Acquired rate limit permissions.");
		} else {
			log.debug("Could not acquire rate limit permissions.");
			throw new RateLimitException("Too many requests from you ip.");
		}
	}

	private Function<String, RateLimiter> createLimit(RateLimit limit) {
		return name -> RateLimiter.create(limit.value());
	}

	private String createKey(JoinPoint jp) {
		Object[] args = jp.getArgs();

		Object last = args[args.length - 1];

		if (last instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) last;

			String ip = request.getHeader("X-FORWARDED-FOR");
			if (ip == null) {
				ip = request.getRemoteAddr();
			}
			return ip;
		} else {
			throw new IllegalArgumentException("To be able to use @RateLimit you must provite HttpServletRequest parameter as the last parameter.");
		}
	}
}
