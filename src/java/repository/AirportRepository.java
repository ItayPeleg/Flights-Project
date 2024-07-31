package com.springflights.repository;

import com.springflights.model.Airport;
import org.springframework.data.repository.CrudRepository;


public interface AirportRepository extends CrudRepository<Airport, Long> {
    Airport findByCode(String departureAirportCode);
}