package com.store.utils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;


@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Receipt {

    private List<Product> content;
    private Integer basePrice;
    private Integer rebateGranted;
    private Integer finalPrice;
}
