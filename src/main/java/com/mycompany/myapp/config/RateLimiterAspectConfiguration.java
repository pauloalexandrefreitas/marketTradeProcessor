package com.mycompany.myapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.mycompany.myapp.aop.ratelimiter.RateLimiterAspect;

@Configuration
@EnableAspectJAutoProxy
public class RateLimiterAspectConfiguration {

	@Bean
	public RateLimiterAspect rateLimiterAspect() {
		return new RateLimiterAspect();
	}
}
