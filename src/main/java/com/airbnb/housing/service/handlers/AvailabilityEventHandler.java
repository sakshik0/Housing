package com.airbnb.housing.service.handlers;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.airbnb.housing.repositories.write.AvailabilityWriteRepository;
import com.airbnb.housing.saga.SagaEvent;
import com.airbnb.housing.saga.SagaEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvailabilityEventHandler {
   private final AvailabilityWriteRepository availabilityWriteRepository;
   
   private final SagaEventPublisher sagaEventPublisher;
   
   
   public void handleBookingConfirmed(SagaEvent event) {
	  try {
		  	Map<String, Object> payload = event.getPayload();
			Long bookingId = Long.valueOf(payload.get("bookingId").toString());
			Long airbnbId = Long.valueOf(payload.get("airbnbId").toString());
			LocalDate checkInDate = LocalDate.parse(payload.get("checkInDate").toString());
			LocalDate checkOutDate = LocalDate.parse(payload.get("checkOutDate").toString());
			
			Long count = availabilityWriteRepository.countByAirbnbIdAndDateBetween(airbnbId, checkInDate.toString(), checkOutDate.toString());
			if(count > 0) {
				sagaEventPublisher.publishEvent("BOOKING_CANCEL_REQUESTED","CANCEL_BOOKING", payload);
				throw new RuntimeException("Availability already booked for airbnbId: " + airbnbId);
			}
			
			//either way find out all the availability records for the airbnbId and date range and update them with the bookingId - but this leads to N+1 problem
			availabilityWriteRepository.updateBookingIdByAirbnbIdAndDateBetween(bookingId, airbnbId, checkInDate, checkOutDate);
			
			sagaEventPublisher.publishEvent("BOOKING_CONFIRMED", "CONFIRM_BOOKING", payload);
	  }
	  catch(Exception e) {
		  e.printStackTrace();
		  throw new RuntimeException("Error handling BOOKING_CONFIRMED event: " + e.getMessage());
		  
	  }
   }
   
   public void handleBookingCancelled(SagaEvent event) {
	   try {
		   	Map<String, Object> payload = event.getPayload();
		    Long bookingId = Long.valueOf(payload.get("bookingId").toString());
			Long airbnbId = Long.valueOf(payload.get("airbnbId").toString());
			LocalDate checkInDate = LocalDate.parse(payload.get("checkInDate").toString());
			LocalDate checkOutDate = LocalDate.parse(payload.get("checkOutDate").toString());
			
			availabilityWriteRepository.updateBookingIdByAirbnbIdAndDateBetween(null, airbnbId, checkInDate, checkOutDate);
			
			sagaEventPublisher.publishEvent("BOOKING_CANCELLED", "CANCEL_BOOKING", payload);
			
	   }
	   catch(Exception e) {
		   e.printStackTrace();
		   throw new RuntimeException("Error handling BOOKING_CANCELLED event: " + e.getMessage());
	   }
   }
   
}
