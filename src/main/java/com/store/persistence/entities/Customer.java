package com.store.persistence.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * Class used to authorization.
 */
@Data
@Entity
@Table(name = "CUSTOMER")
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

}

