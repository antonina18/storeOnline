package com.store.persistence.repositories;

import com.store.persistence.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findById(Long id);


}
