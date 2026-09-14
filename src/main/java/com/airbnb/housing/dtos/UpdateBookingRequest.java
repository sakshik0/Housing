package com.airbnb.housing.dtos;

import com.airbnb.housing.utils.BookingStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBookingRequest {
	
	@NotNull(message = "Booking ID is required")
	private Long id;
	
	@NotNull(message = "Idempotency key is required")
	private Long idempotencyKey;
	
	@NotNull(message = "Booking status is required")
	private BookingStatus bookingStatus;
}
