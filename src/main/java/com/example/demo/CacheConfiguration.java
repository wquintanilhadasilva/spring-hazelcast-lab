package com.example.demo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "nuvem.cache.hazelcast.enabled", matchIfMissing = true)
public class CacheConfiguration {
	public CacheConfiguration() {
		System.out.println("=================================>>>>>>>>>>>>> Start CACHE OK?! ====>");
	}
}
