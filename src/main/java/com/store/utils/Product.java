package com.store.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

    private String name;
    private Integer units;
    private Integer priceWithoutPromotion;
}
