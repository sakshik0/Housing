package com.airbnb.housing.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.airbnb.housing.dtos.CreateBookingRequest;
import com.airbnb.housing.dtos.UpdateBookingRequest;
import com.airbnb.housing.models.Airbnb;
import com.airbnb.housing.models.Availability;
import com.airbnb.housing.models.Booking;
import com.airbnb.housing.repositories.write.AirbnbWriteRepository;
import com.airbnb.housing.repositories.write.BookingWriteRepository;
import com.airbnb.housing.service.concurrency.ConcurrencyControl;
import com.airbnb.housing.utils.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class BookingService implements IBookingService {
	
	private final AirbnbWriteRepository airbnbWriteRepository;
	private final BookingWriteRepository bookingWriteRepository;
	private final AirbnbWriteRepository availabilityWriteRepository;
	private final ConcurrencyControl concurrencyControl;
	
	@Override
	public Booking createBooking(CreateBookingRequest request) {
		
		Airbnb airbnb = airbnbWriteRepository.findById(request.getAirbnbId()).orElseThrow(() -> new RuntimeException("Airbnb not found"));
		
		if(request.getCheckInDate().isAfter(request.getCheckOutDate())) {
			throw new RuntimeException("Check-in date cannot be after check-out date");
		}
		
		if(request.getCheckInDate().isBefore(LocalDate.now())) {
			throw new RuntimeException("Check-in date cannot be in the past");
		}
		
		
		List<Availability> availabilityList = concurrencyControl.lockAndCheckAvailability(request.getAirbnbId(), request.getCheckInDate().toString(), request.getCheckOutDate().toString(), request.getUserId());
		
		long nights= ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
		
		double pricePerNight = airbnb.getPricePerNight();
		
		double totalPrice = pricePerNight * nights;
		
		String idempotencyKey = UUID.randomUUID().toString();
		
		log.info("Creating booking for Airbnb ID: {}, User ID: {}, Total Price: {} , IdempotencyKey : {}", request.getAirbnbId(), request.getUserId(), totalPrice , idempotencyKey);
		
		Booking booking = Booking.builder()
				.airbnbId(request.getAirbnbId())
				.userId(request.getUserId())
				.totalPrice(totalPrice)
				.idempotencyKey(idempotencyKey)
				.bookingStatus(BookingStatus.PENDING)
				.checkInDate(request.getCheckInDate())
				.checkOutDate(request.getCheckOutDate())
				.build();
		
		
		booking = bookingWriteRepository.save(booking);
		
		
		return booking;
	}

	@Override
	public Booking updateBooking(UpdateBookingRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
