package com.store.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    private String name;
    private Integer amount;
    private Integer priceWithoutPromotion;
    private Integer promotionRefund;
    private Integer finalPrice;
}
