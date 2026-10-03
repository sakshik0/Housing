package com.airbnb.housing.service;

import java.util.Optional;

import com.airbnb.housing.models.Booking;

public interface IIdempotencyService {
	
	boolean isIdempotencyKeyUsed(String idempotencyKey);
	
	Optional<Booking> getBookingByIdempotencyKey(String idempotencyKey);
}
