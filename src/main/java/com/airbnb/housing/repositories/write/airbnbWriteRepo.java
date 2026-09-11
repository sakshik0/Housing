package com.airbnb.housing.repositories.write;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airbnb.housing.models.Airbnb;

@Repository
public interface airbnbWriteRepo extends JpaRepository<Airbnb, Long> {

}
