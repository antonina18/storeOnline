package com.store.stub;


import com.store.dto.BuyItemDto;
import com.store.persistence.entities.Item;

public class ItemObjectMother {


    public static Item item(String name, Integer price){
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        return item;
    }

    public static BuyItemDto buyItemDto(String name, Integer units){
        BuyItemDto buyItemDto = new BuyItemDto();
        buyItemDto.setName(name);
        buyItemDto.setUnits(units);
        return buyItemDto;
    }
}
