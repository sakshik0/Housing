package com.airbnb.housing.repositories.write;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airbnb.housing.models.Availability;

@Repository
public interface AvailablityWriteRepository extends JpaRepository<Availability, Long> {
	
	List<Availability> findByAirbnbId(Long airbnbId);
	
	List<Availability> findByBookingId(Long bookingId);
	
	List<Availability> findByAirbnbIdAndDateBetween(Long airbnbId, String checkInDate, String checkOutDate);
	
	//Select count(*) from availability where airbnb_id = ? and date between ? and ? and booking_id is not null
	Long countByAirbnbIdAndDateBetween(Long airbnbId, String checkInDate, String checkOutDate);
}
