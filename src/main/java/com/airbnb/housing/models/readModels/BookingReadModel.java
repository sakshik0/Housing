package com.airbnb.housing.models.readModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingReadModel {
	
	private Long bookingId;
	
	private Long userId;
	
	private Long airbnbId;
	
	private String totalPrice;
	
	private String bookingStatus;
	
	private String idempotencyKey;
	
	private String checkInDate;
	
	private String checkOutDate;
}
