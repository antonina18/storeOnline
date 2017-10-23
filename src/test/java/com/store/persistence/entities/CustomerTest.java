package com.store.persistence.entities;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

public class CustomerTest {

    private Customer customer;

    @Before
    public void setUp() throws Exception {
        customer = new Customer();
        Basket basket = new Basket();
        basket.put(new Item(), 1);
        customer.setBasket(basket);
    }

    @Test
    public void testClearBasket() throws Exception {
        //given
        Basket basket = customer.getBasket();

        //when
        customer.clearBasket();

        //then
        assertNotEquals(basket, customer.getBasket());
    }

}