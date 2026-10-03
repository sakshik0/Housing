package com.airbnb.housing.saga;

import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SagaEventPublisher {
	
	private static final String SAGA_QUEUE = "saga:event";
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;
	
	void publishEvent(SagaEvent event,String Step,Map<String,Object> payload) {
		SagaEvent sagaEvent = SagaEvent.builder()
				.sagaId(event.getSagaId())
				.eventType(event.getEventType())
				.step(Step)
				.payload(payload)
				.timestamp(event.getTimestamp())
				.status(SagaEvent.SagaStatus.PENDING)
				.build();
		try {
			String eventJson = objectMapper.writeValueAsString(sagaEvent);
			redisTemplate.opsForList().rightPush(SAGA_QUEUE, eventJson);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to publish saga event", e);
		}
	}
}
	