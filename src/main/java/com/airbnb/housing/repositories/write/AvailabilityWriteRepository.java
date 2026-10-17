package com.airbnb.housing.repositories.write;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.airbnb.housing.models.Availability;

@Repository
public interface AvailabilityWriteRepository extends JpaRepository<Availability, Long> {
	
	List<Availability> findByAirbnbId(Long airbnbId);
	
	List<Availability> findByBookingId(Long bookingId);
	
	List<Availability> findByAirbnbIdAndDateBetween(Long airbnbId, String checkInDate, String checkOutDate);
	
	//Select count(*) from availability where airbnb_id = ? and date between ? and ? and booking_id is not null
	Long countByAirbnbIdAndDateBetween(Long airbnbId, String checkInDate, String checkOutDate);
	
	//Update availability set booking_id = ? where airbnb_id = ? and date between ? and ?
	 @Modifying
	 @Query("UPDATE Availability a SET a.bookingId = :bookingId WHERE a.airbnbId = :airbnbId AND a.date BETWEEN :checkInDate AND :checkOutDate")
	 void updateBookingIdByAirbnbIdAndDateBetween(Long bookingId, Long airbnbId, LocalDate checkInDate, LocalDate checkOutDate);
	
}
