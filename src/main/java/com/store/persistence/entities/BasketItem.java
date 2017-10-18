package com.store.persistence.entities;

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

}

