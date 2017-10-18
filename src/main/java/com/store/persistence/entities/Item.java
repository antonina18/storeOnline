package com.store.persistence.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ITEM_ID")
    private SpecialPrice promotion;

}
