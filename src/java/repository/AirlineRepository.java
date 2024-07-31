package com.springflights.repository;

import com.springflights.model.Airline;
import org.springframework.data.repository.CrudRepository;

public interface AirlineRepository extends CrudRepository<Airline, Long> {

    Airline findByName(String name);
}
