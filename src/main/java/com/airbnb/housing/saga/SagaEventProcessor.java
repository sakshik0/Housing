package com.airbnb.housing.saga;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SagaEventProcessor {
	
	public void processEvent(SagaEvent event) {
		// Implement the logic to process the saga event
		System.out.println("Processing saga event: " + event);
	}
}
