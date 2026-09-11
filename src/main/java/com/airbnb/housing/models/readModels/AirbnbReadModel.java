package com.airbnb.housing.models.readModels;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirbnbReadModel {
	
	private Long Id;
	
	private String name;
	
	private String description;
	
	private String location;
	
	private String pricePerNight;
	
	private List<AvailabilityReadModel> availability;
}
