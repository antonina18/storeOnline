package com.store.persistence.entities;

import com.store.utils.Product;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.store.stub.ItemObjectMother.item;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BasketTest {

    private Basket basket;

    @Before
    public void setUp() throws Exception {
        basket = new Basket();
    }

    @Test
    public void simplePut() throws Exception {
        //given
        int expected = 1;
        Item item = new Item();

        //when
        basket.put(item, 1);

        //then
        assertThat(basket.getContent().size(), is(equalTo(expected)));
    }

    @Test
    public void putAndIncrementShouldHaveOneItem() throws Exception {
        //given
        Item item = new Item();

        //when
        basket.put(item, 1);
        basket.put(item, 2);

        //then
        assertThat(basket.getContent().size(), is(equalTo(1)));
        assertThat(basket.getContent().iterator().next().getUnits(), is(equalTo(3)));
    }

    @Test
    public void putAndIncrementShouldHaveTwoItems() throws Exception {
        //given
        Item firstItem = item("A", 1);
        Item secondItem = item("B", 1);

        //when
        basket.put(firstItem, 1);
        basket.put(secondItem, 2);

        //then
        assertThat(basket.getContent().size(), is(equalTo(2)));
    }

    @Test
    public void getProducts() throws Exception {
        //given
        BasketItem basketItem = new BasketItem(item("B", 1), 2);
        BasketItem basketItem2 = new BasketItem(item("A", 1), 2);
        prepareBasket(basketItem, basketItem2);
        List<Product> expected = Stream.of(basketItem.toProduct(), basketItem2.toProduct())
            .collect(Collectors.toList());

        //when
        List<Product> actual = basket.getProducts();

        //then
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void testScanPrice() throws Exception {
        //given
        BasketItem basketItem = new BasketItem(item("B", 1), 2);
        BasketItem basketItem2 = new BasketItem(item("A", 1), 2);
        prepareBasket(basketItem, basketItem2);
        Integer expected = basketItem.countPrice() + basketItem2.countPrice();

        //when
        Integer actual = basket.scanPrice();

        //then
        assertThat(actual, is(equalTo(expected)));
    }

    private void prepareBasket(BasketItem... basketItems) {
        Set<BasketItem> collect = Arrays.stream(basketItems).collect(Collectors.toSet());
        basket.setContent(new HashSet<>(collect));
    }




}