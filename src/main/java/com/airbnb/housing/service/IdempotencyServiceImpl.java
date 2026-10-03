package com.airbnb.housing.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.airbnb.housing.models.Booking;
import com.airbnb.housing.models.readModels.BookingReadModel;
import com.airbnb.housing.repositories.read.RedisReadRepository;
import com.airbnb.housing.utils.BookingStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdempotencyServiceImpl implements IIdempotencyService {
	
	private final RedisReadRepository redisReadRepository;
	@Override
	public boolean isIdempotencyKeyUsed(String idempotencyKey) {
		// Implement logic to check if the idempotency key has been used
		
		return false; // Placeholder return value
	}

	@Override
	public Optional<Booking> getBookingByIdempotencyKey(String idempotencyKey) {
		
		BookingReadModel bookingReadModel = redisReadRepository.getBookingByIdempotencyKey(idempotencyKey);
		
		if(bookingReadModel==null) {
			throw new RuntimeException("Booking not found for idempotency key: " + idempotencyKey);
		}
		
		Booking booking = Booking.builder()
				.id(bookingReadModel.getBookingId())
				.airbnbId(bookingReadModel.getAirbnbId())
				.userId(bookingReadModel.getUserId())
				.totalPrice(bookingReadModel.getTotalPrice())
				.checkInDate(bookingReadModel.getCheckInDate())
				.checkOutDate(bookingReadModel.getCheckOutDate())
				.bookingStatus(BookingStatus.valueOf(bookingReadModel.getBookingStatus()))
				.idempotencyKey(bookingReadModel.getIdempotencyKey())
				.build();
		
		return Optional.of(booking); // Placeholder return value
	}

}
