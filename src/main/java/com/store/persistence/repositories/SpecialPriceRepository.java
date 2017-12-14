package com.store.persistence.repositories;

import com.store.persistence.entities.SpecialPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialPriceRepository extends JpaRepository<SpecialPrice, Long> {

    Optional<SpecialPrice> findById(Integer id);
}
