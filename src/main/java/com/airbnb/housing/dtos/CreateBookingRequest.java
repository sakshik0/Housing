package com.airbnb.housing.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateBookingRequest {
	
	@NotNull(message = "Airbnb ID is required")
	private Long airbnbId;
	
	@NotNull(message = "User ID is required")
	private Long userId;
	
	@NotNull(message = "Check-in is required")
	private LocalDate checkInDate;
	
	@NotNull(message = "Check-out is required") 
	private LocalDate checkOutDate;
}
