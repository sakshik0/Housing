package com.airbnb.housing.models;

import java.time.LocalDate;

import com.airbnb.housing.utils.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "booking")
@Builder
@AllArgsConstructor
@Data
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long userId;
	
	@Column(nullable = false)
	private Long airbnbId;

	private Double totalPrice;

	@Column(nullable = false)
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private BookingStatus bookingStatus=BookingStatus.PENDING; // e.g., "confirmed", "cancelled", "pending"

	@Column(nullable = false, unique = true)
	private String idempotencyKey; // Unique key to prevent duplicate bookings
	
	@Column(nullable = false)
	private LocalDate checkInDate; // Store as String for simplicity, can be changed to LocalDate if needed
	
	@Column(nullable = false)
	private LocalDate checkOutDate; // Store as String for simplicity, can be changed to LocalDate if needed

}
