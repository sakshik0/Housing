package com.airbnb.housing.service.concurrency;

import java.util.List;

import com.airbnb.housing.models.readModels.AvailabilityReadModel;

public interface concurrencyControl {
	
	 void realeaseLock(Long airbnbId,String checkInDate, String checkOutDate);
	 
	 List<AvailabilityReadModel> lockAndCheckAvailability(Long airbnbId,String checkInDate, String checkOutDate);
}
