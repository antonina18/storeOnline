package com.store.persistence.repositories;

import com.store.persistence.entities.Item;
import com.store.persistence.entities.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    Optional<Item> findItemByName(String name);

    void deleteMagazineByItems(Item item);

    Magazine findById(Long id);

}
