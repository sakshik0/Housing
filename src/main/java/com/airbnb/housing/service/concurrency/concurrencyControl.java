package com.airbnb.housing.service.concurrency;

import java.util.List;

import com.airbnb.housing.models.Availability;

public interface ConcurrencyControl {
	
	 void realeaseLock(Long airbnbId,String checkInDate, String checkOutDate);
	 
	 List<Availability> lockAndCheckAvailability(Long airbnbId,String checkInDate, String checkOutDate ,Long userId); 
}
