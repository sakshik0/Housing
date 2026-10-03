package com.airbnb.housing.repositories.read;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.airbnb.housing.models.Booking;
import com.airbnb.housing.models.readModels.BookingReadModel;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
public class RedisWriteRepository {
	
	private final ObjectMapper objectMapper;
	private final RedisTemplate<String,String> redisTemplate;
	
	public void writeBookingReadModelToRedis(Booking booking) {
		// Implement the logic to write the booking read model to Redis
		
		BookingReadModel bookingreadModel = BookingReadModel.builder()
				.airbnbId(booking.getAirbnbId())
				.bookingId(booking.getId())
				.userId(booking.getUserId())
				.totalPrice(booking.getTotalPrice())
				.checkInDate(booking.getCheckInDate())
				.checkOutDate(booking.getCheckOutDate())
				.bookingStatus(booking.getBookingStatus().name())
				.idempotencyKey(booking.getIdempotencyKey())
				.build();
		
		saveBookingReadModelToRedis(bookingreadModel);
	}
	
	private void saveBookingReadModelToRedis(BookingReadModel bookingReadModel) {
		String key = RedisReadRepository.BOOKING_KEY_PREFIX + bookingReadModel.getBookingId();
		String value = objectMapper.writeValueAsString(bookingReadModel);
		redisTemplate.opsForValue().set(key, value);
	}
}
