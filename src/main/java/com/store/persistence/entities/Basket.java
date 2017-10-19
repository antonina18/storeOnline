package com.store.persistence.entities;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

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


}
