package com.airbnb.housing.service.handlers;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.airbnb.housing.models.Booking;
import com.airbnb.housing.repositories.write.BookingWriteRepository;
import com.airbnb.housing.saga.SagaEvent;
import com.airbnb.housing.saga.SagaEventPublisher;
import com.airbnb.housing.utils.BookingStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingEventHandler {
	
	private final BookingWriteRepository bookingWriteRepository;
	private final SagaEventPublisher sagaEventPublisher;
	
	public void handleBookingConfirmRequested(SagaEvent event) {
		try {
		Map<String, Object> payload = event.getPayload();
		Long bookingId = Long.valueOf(payload.get("bookingId").toString());
		Long airbnbId = Long.valueOf(payload.get("airbnbId").toString());
		LocalDate checkInDate = LocalDate.parse(payload.get("checkInDate").toString());
		LocalDate checkOutDate = LocalDate.parse(payload.get("checkOutDate").toString());
		
		Booking booking = bookingWriteRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
		booking.setBookingStatus(BookingStatus.CONFIRMED);
		bookingWriteRepository.save(booking);
		
		sagaEventPublisher.publishEvent("BOOKING_CONFIRMED","CONFIRM_BOOKING", Map.of(
				"bookingId", bookingId,
				"airbnbId", airbnbId,
				"checkInDate", checkInDate.toString(),
				"checkOutDate", checkOutDate.toString()
		));
		} catch (Exception e) {
			Map<String, Object> payload = event.getPayload();
			sagaEventPublisher.publishEvent("BOOKING_COMPENSATED","COMPENSATE_BOOKING", payload);
			e.printStackTrace();
			throw new RuntimeException("Error handling BOOKING_CONFIRM_REQUESTED event: " + e.getMessage());
			// Handle exception, possibly publish a failure event
		}
	}
	
	public void handleBookingCancelRequested(SagaEvent event) {
		try {
		   Map<String, Object> payload = event.getPayload();
		   	Long bookingId = Long.valueOf(payload.get("bookingId").toString());
			Long airbnbId = Long.valueOf(payload.get("airbnbId").toString());
			LocalDate checkInDate = LocalDate.parse(payload.get("checkInDate").toString());
			LocalDate checkOutDate = LocalDate.parse(payload.get("checkOutDate").toString());
			
			Booking booking = bookingWriteRepository.findById(bookingId)
					.orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
			 
			sagaEventPublisher.publishEvent("BOOKING_CANCELLED","CANCEL_BOOKING", Map.of(
					"bookingId", bookingId,
					"airbnbId", airbnbId,
					"checkInDate", checkInDate.toString(),
					"checkOutDate", checkOutDate.toString()
			));
	} catch (Exception e) {
		Map<String, Object> payload = event.getPayload();
		sagaEventPublisher.publishEvent("BOOKING_COMPENSATED","COMPENSATE_BOOKING", payload);
		e.printStackTrace();
		throw new RuntimeException("Error handling BOOKING_CONFIRM_REQUESTED event: " + e.getMessage());
		// Handle exception, possibly publish a failure event
	}
		
	}
}
