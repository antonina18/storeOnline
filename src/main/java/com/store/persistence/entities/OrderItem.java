package com.store.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem {


    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "UNITS")
    private Integer units;

    public OrderItem(String name, Integer units) {
        this.name = name;
        this.units = units;
    }
}
