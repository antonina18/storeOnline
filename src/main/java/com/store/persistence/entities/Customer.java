package com.store.persistence.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

/**
 * Class used to authorization.
 */
@Data
@Entity
@Table(name = "CUSTOMER")
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BASKET_ID")
    private Basket basket;

    public Customer(String name) {
        this.name = name;
        this.basket = new Basket();
    }

    public void clearBasket() {
        this.basket = new Basket();
    }

    public Integer rebatePromotionItems(Basket basket, List<PromotionItems> promotionItems) {
        Set<String> collect = basket.getContent()
            .stream()
            .map(BasketItem::getItem)
            .map(Item::getName)
            .collect(Collectors.toSet());

        return promotionItems.stream()
            .filter(e-> collect.contains(e.getItemX()) && collect.contains(e.getItemY()))
            .mapToInt(PromotionItems::getDiscount)
            .sum();
    }

}

