package com.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;
    private String name;
    private Integer price;
    private boolean specialPrice;
    private Integer units;

    public ItemDto(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public ItemDto(Long id, String name, Integer price, boolean specialPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.specialPrice = specialPrice;
    }
}
