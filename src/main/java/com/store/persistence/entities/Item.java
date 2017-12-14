package com.store.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import javax.persistence.*;

/**
 * Holds item.
 */
@Entity
@NoArgsConstructor
@Data
@Table(name = "ITEM")
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "PRICE", nullable = false)
    private Integer price;

    @Column(name = "UNIT", nullable = false)
    private Integer unit;

    @Column(name = "SP_ID")
    private Integer specialPriceId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="MAGAZINE_ID")
    private Magazine magazine;

    public Item(int price, String name) {
        this.price = price;
        this.name = name;
    }

    public Item(Long id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Item(Long id, Integer price, String name, Integer unit) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.unit = unit;
    }
}
