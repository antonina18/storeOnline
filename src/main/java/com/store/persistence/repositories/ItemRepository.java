package com.store.persistence.repositories;

import com.store.persistence.entities.Item;
import com.store.persistence.entities.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByName(String name);

    Optional<Item> findByMagazineName(String name);

    Optional<Item> findById(Long id);

    Optional<Item> findByMagazine(Magazine magazine);
}
