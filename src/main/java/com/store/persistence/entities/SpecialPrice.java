package com.store.persistence.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "SPECIAL_PRICE")
@NoArgsConstructor
public class SpecialPrice {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Integer id;

    @Column(name = "UNIT")
    private Integer unit;

    @Column(name = "PRICE")
    private Integer price;

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public SpecialPrice(Integer unit, Integer price) {
        this.unit = unit;
        this.price = price;
    }
}
