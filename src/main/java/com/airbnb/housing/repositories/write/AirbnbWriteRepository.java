package com.airbnb.housing.repositories.write;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airbnb.housing.models.Airbnb;

@Repository
public interface AirbnbWriteRepository extends JpaRepository<Airbnb, Long> {
	
	Optional<Airbnb> findById(Long id);
}
