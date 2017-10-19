package com.store.persistence.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class holds item promotion.
 */
@Entity
@Data
@Table(name = "PROMOTION_ITEMS")
@NoArgsConstructor
public class PromotionItems {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "ITEM_X")
    private String itemX;

    @Column(name = "ITEM_Y")
    private String itemY;

    @Column(name = "DISCOUNT")
    private Integer discount;


}
