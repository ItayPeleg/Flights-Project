package com.springflights.repository;

import com.springflights.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer findByUsername(String username);

    // Custom method to check if username exists
    boolean existsByUsername(String username);
}
