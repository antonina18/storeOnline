package com.store.persistence.repositories;

import com.store.persistence.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByName(String admin);
}
