package com.store.persistence.entities;

import com.store.utils.Product;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

/**
 * Class used as basket with content.
 */
@Data
@Entity
@Table(name = "BASKET")
public class Basket {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "BASKET_ID", referencedColumnName = "ID")
    private Set<BasketItem> content;

    public Basket() {
        this.content = new HashSet<>();
    }

    public void put(Item item, int unit) {
        Optional<BasketItem> optionalItem = getItem(item);
        if (optionalItem.isPresent()) {
            optionalItem.get().incrementUnitsBy(unit);
        } else {
            content.add(new BasketItem(item, unit));
        }
    }

    private Optional<BasketItem> getItem(Item item) {
        return content.stream()
            .filter(basketItem -> basketItem.getItem().equals(item))
            .findFirst();
    }

    public List<Product> getProducts() {
        return content.stream()
            .map(BasketItem::toProduct)
            .collect(Collectors.toList());
    }

    public Integer scanPrice() {
        return content
            .stream()
            .map(BasketItem::countPrice)
            .mapToInt(Integer::intValue)
            .sum();
    }

}
