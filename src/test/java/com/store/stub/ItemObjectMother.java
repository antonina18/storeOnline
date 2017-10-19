package com.store.stub;


import com.store.persistence.entities.Item;
import com.store.persistence.entities.SpecialPrice;

public class ItemObjectMother {


    public static Item item(String name, Integer price){
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        return item;
    }

    public static Item itemWithSpecialPrice(String name, Integer price, SpecialPrice specialPrice){
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setPromotion(specialPrice);

        return item;
    }

    public static SpecialPrice specialPrice(Integer unit, Integer price){
        SpecialPrice specialPrice = new SpecialPrice();
        specialPrice.setUnit(unit);
        specialPrice.setPrice(price);

        return specialPrice;
    }
}
