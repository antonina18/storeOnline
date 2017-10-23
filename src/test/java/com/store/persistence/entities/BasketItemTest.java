package com.store.persistence.entities;


import com.store.utils.Product;
import org.junit.Before;
import org.junit.Test;

import static com.store.stub.ItemObjectMother.item;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BasketItemTest {

    private BasketItem basketItem;

    @Before
    public void setUp() throws Exception {
        basketItem = new BasketItem();
    }

    @Test
    public void countPrice() throws Exception {
        //given
        Integer expected = 20;
        Item banana = item("banana", 4);
        basketItem.setItem(banana);
        basketItem.setUnits(5);

        //when
        Integer actual = basketItem.countPrice();

        //then
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void toProduct() throws Exception {
        //given
        Product expected = new Product("banana", 5, 20);
        Item banana = item("banana", 4);
        basketItem.setItem(banana);
        basketItem.setUnits(5);

        //when
        Product actual = basketItem.toProduct();

        //then
        assertThat(actual, is(equalTo(expected)));
    }

}