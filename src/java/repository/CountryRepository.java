package com.springflights.repository;

import com.springflights.model.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Long> {
    Country findByName(String destinationCountryName);
}
