package com.airbnb.housing.saga;

import org.springframework.stereotype.Component;

import com.airbnb.housing.service.handlers.AvailabilityEventHandler;
import com.airbnb.housing.service.handlers.BookingEventHandler;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SagaEventProcessor {
	
	private final BookingEventHandler bookingEventHandler;
	private final AvailabilityEventHandler availabilityEventHandler;
	public void processEvent(SagaEvent event) {
		switch(event.getEventType()) {
			case "BOOKING_CREATED":
				// no action recquired for booking created event
				break;
			case "BOOKING_CONFIRM_REQUESTED":
				bookingEventHandler.handleBookingConfirmRequested(event);
				break;
			case "BOOKING_CONFIRMED":
				availabilityEventHandler.handleBookingConfirmed(event);
				// Handle booking cancelled event
				
				break;
			case "BOOKING_CANCEL_REQUESTED":
				bookingEventHandler.handleBookingCancelRequested(event);
				break;
			case "BOOKING_CANCELLED":
				availabilityEventHandler.handleBookingCancelled(event);
				// Handle booking cancelled event
				break;
			case "BOOKING_COMPENSATED":
				// Handle booking cancelled event
				break;
			default:
				throw new IllegalArgumentException("Unknown event type: " + event.getEventType());
		}
	}
}
