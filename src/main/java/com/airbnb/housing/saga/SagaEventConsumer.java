package com.airbnb.housing.saga;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SagaEventConsumer {
	private static final String SAGA_QUEUE = "saga:event";
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;
	
	@Scheduled(fixedDelay = 500) //execute the task periodically //whenever an update on booking come , we will consume the event till then keep checking the publisher queue
	public void eventConsumer() {
		try {
			String eventJson = redisTemplate.opsForList().leftPop(SAGA_QUEUE);
			if (eventJson != null) {
				SagaEvent sagaEvent = objectMapper.readValue(eventJson, SagaEvent.class);
				
				System.out.println("Processing saga event: " + sagaEvent);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to consume saga event", e);
		}
	}
	
}
