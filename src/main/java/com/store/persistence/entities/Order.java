package com.store.persistence.entities;

import com.store.dto.BuyItemDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Holds item.
 */
@Entity
@NoArgsConstructor
@Data
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "REALIZED", columnDefinition = "boolean default false")
    private Boolean realized;

    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="ITEM_NAME")
    private List<OrderItem> itemList = new ArrayList<>();

}
