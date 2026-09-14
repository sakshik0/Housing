package com.airbnb.housing.service.concurrency;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.airbnb.housing.models.Availability;
import com.airbnb.housing.repositories.write.AvailablityWriteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisLockStrategy implements ConcurrencyControl {
		
	private static final String LOCK_KEY_PREFIX = "lock:availability:";
	private static final Duration LOCK_EXPIRATION_TIME = Duration.ofMinutes(2); // Set lock expiration time to 2 minutes
	private final RedisTemplate<String, String> redisTemplate;
	private final AvailablityWriteRepository availabilityWriteRepository;
	
	@Override
	public void realeaseLock(Long airbnbId, String checkInDate, String checkOutDate) {
		// Implement Redis lock release logic here
		String lockKey = getLockKey(airbnbId, checkInDate, checkOutDate);
		
		String lockValue = redisTemplate.opsForValue().get(lockKey);
		
		if(lockValue != null) {
			redisTemplate.delete(lockKey);
		}
	}

	@Override
	public List<Availability> lockAndCheckAvailability(Long airbnbId, String checkInDate, String checkOutDate,Long userId) {
		
		Long count = availabilityWriteRepository.countByAirbnbIdAndDateBetween(airbnbId, checkInDate, checkOutDate);
		
		if(count>0) {
			throw new RuntimeException("Availability not available for the given dates");
		}
		
		String lockKey = getLockKey(airbnbId, checkInDate, checkOutDate);
		Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, userId.toString(), LOCK_EXPIRATION_TIME);
		
		if(!lockAcquired)
		{
			throw new IllegalStateException("Could not acquire lock for availability check. Please try again later.");
		}
		
		try {
			return availabilityWriteRepository.findByAirbnbIdAndDateBetween(airbnbId, checkInDate, checkOutDate);
		}
		catch (Exception e) {
			throw new RuntimeException("Error while checking availability: " + e.getMessage(), e);
		} finally {
			realeaseLock(airbnbId, checkInDate, checkOutDate);
		} 
	}
	
	private String getLockKey(Long airbnbId, String checkInDate, String checkOutDate) {
		return LOCK_KEY_PREFIX + airbnbId + ":" + checkInDate + ":" + checkOutDate;
	}

}
