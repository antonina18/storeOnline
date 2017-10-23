package com.store.persistence.entities;

import com.store.utils.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class used as basket content.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "BASKET_ITEM")
public class BasketItem {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @Column(name = "UNITS", nullable = false)
    private Integer units;

    public BasketItem(Item item, int units) {
        this.item = item;
        this.units = units;
    }

    public void incrementUnitsBy(int toIncrement) {
        units += toIncrement;
    }

    public Product toProduct() {
        return new Product(item.getName(), units, countPrice());
    }

    public Integer countPrice() {
        return (item.getPrice() * units);
    }



}

