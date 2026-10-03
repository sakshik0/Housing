package com.airbnb.housing.saga;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SagaEvent implements Serializable {
	private Integer sagaId;
	
	private String eventType;
	
	private String step;
	
	private Map<String, Object> payload;
	
	private LocalDateTime timestamp;
	
	private SagaStatus status;

	public enum SagaStatus {
		PENDING,
		COMPLETED,
		FAILED
	}
}
