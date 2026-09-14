package com.airbnb.housing.service;

import org.springframework.stereotype.Service;

import com.airbnb.housing.dtos.CreateBookingRequest;
import com.airbnb.housing.dtos.UpdateBookingRequest;
import com.airbnb.housing.models.Booking;

@Service
public interface IBookingService {
	
	Booking createBooking(CreateBookingRequest request);
	
	Booking updateBooking(UpdateBookingRequest request);
	
}
