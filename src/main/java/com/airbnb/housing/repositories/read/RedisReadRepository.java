package com.airbnb.housing.repositories.read;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.airbnb.housing.models.readModels.AirbnbReadModel;
import com.airbnb.housing.models.readModels.AvailabilityReadModel;
import com.airbnb.housing.models.readModels.BookingReadModel;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;


@Repository
@RequiredArgsConstructor
public class RedisReadRepository {
	
	public static final String AIRBNB_KEY_PREFIX = "airbnb:";
	public static final String BOOKING_KEY_PREFIX = "booking:";
	public static final String AVAILABILITY_KEY_PREFIX = "availablity:";
	private RedisTemplate<String,String> redisTemplate;
	private  final ObjectMapper objectMapper;
	
	public AirbnbReadModel getAirbnbById(String id) {
		String key = AIRBNB_KEY_PREFIX + id;
		String value = redisTemplate.opsForValue().get(key);
		if (value != null) {
			try {
				return objectMapper.readValue(value, AirbnbReadModel.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public List<AirbnbReadModel> getAllAirbnbs() {
		List<AirbnbReadModel> airbnbs = new ArrayList<>();
		Set<String> keys = redisTemplate.keys(AIRBNB_KEY_PREFIX + "*"); //Prefix of the Redis is used to get all keys related to Airbnb
		if (keys != null) {
			for (String key : keys) {
				String value = redisTemplate.opsForValue().get(key);
				if (value != null) {
					try {
						AirbnbReadModel airbnb = objectMapper.readValue(value, AirbnbReadModel.class);
						airbnbs.add(airbnb);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return airbnbs;
	}
	
	public BookingReadModel getBookingById(String id) {
		String key = BOOKING_KEY_PREFIX + id;
		String value = redisTemplate.opsForValue().get(key);
		if (value != null) {
			try {
				return objectMapper.readValue(value, BookingReadModel.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public AvailabilityReadModel getAvailabilityById(String id) {
		String key=AVAILABILITY_KEY_PREFIX + id;
		
		String value= redisTemplate.opsForValue().get(key);
		
		if(value!=null) {
			try {
				return objectMapper.readValue(value, AvailabilityReadModel.class);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public BookingReadModel getBookingByIdempotencyKey(String idempotencyKey) {
		Set<String> keys = redisTemplate.keys(BOOKING_KEY_PREFIX + "*"); //get all entries in the booking read model
//		if (keys != null) {
//			for (String key : keys) {
//				String value = redisTemplate.opsForValue().get(key);
//				if (value != null) {
//					try {
//						BookingReadModel booking = objectMapper.readValue(value, BookingReadModel.class);
//						if (booking.getIdempotencyKey().equals(idempotencyKey)) {
//							return booking;
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
		
		if(keys ==null || keys.isEmpty())
		{
			return null;
		}
		
		
		return keys.stream().map(key -> {
			String value = redisTemplate.opsForValue().get(key);
			if (value != null) {
				try {
					BookingReadModel booking = objectMapper.readValue(value, BookingReadModel.class);
					if (booking.getIdempotencyKey().equals(idempotencyKey)) {
						return booking;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;}).filter(booking -> booking != null).findFirst().orElse(null);
	}
	
}
