package com.springflights.repository;

import com.springflights.model.Customer;
import com.springflights.model.Purchase;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {
    List<Purchase> findByCustomer(Customer customer);

    List<Purchase> findByCustomerId(Long id);
}
