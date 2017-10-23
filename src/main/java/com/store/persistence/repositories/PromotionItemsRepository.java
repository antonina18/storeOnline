package com.store.persistence.repositories;

import com.store.persistence.entities.PromotionItems;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PromotionItemsRepository extends JpaRepository<PromotionItems, Long> {

}
