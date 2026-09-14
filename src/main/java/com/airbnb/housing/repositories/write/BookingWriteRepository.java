package com.airbnb.housing.repositories.write;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.airbnb.housing.models.Booking;

import jakarta.persistence.LockModeType;

@Repository
public interface BookingWriteRepository extends JpaRepository<Booking, Long> {
	
	 List<Booking> findbyAirbnbId(Long airbnbId);
	 
	 @Lock(value = LockModeType.PESSIMISTIC_WRITE)
	 @Query("SELECT b FROM Booking b WHERE b.id = :bookingId")
	 Optional<Booking> findById(@Param("bookingId") Long bookingId);
}
