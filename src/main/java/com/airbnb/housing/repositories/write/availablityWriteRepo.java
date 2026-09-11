package com.airbnb.housing.repositories.write;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airbnb.housing.models.Availability;

@Repository
public interface availablityWriteRepo extends JpaRepository<Availability, Long> {
	
	List<Availability> findByAirbnbId(Long airbnbId);
	
	List<Availability> findByBookingId(Long bookingId);
}
