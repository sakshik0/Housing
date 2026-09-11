package com.airbnb.housing.models.readModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityReadModel {

	private Long Id;
	
	private Long airbnbId;
	
	private String date;
	
	private Long bookingId;
	
	private Boolean isAvailable;
}
