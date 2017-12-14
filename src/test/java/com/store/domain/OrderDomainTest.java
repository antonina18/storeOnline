package com.store.domain;

import com.store.dto.BuyItemDto;
import com.store.persistence.entities.Item;
import com.store.persistence.entities.Order;
import com.store.persistence.entities.OrderItem;
import com.store.persistence.repositories.ItemRepository;
import com.store.persistence.repositories.OrderRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;

public class OrderDomainTest {

    private ItemRepository itemRepository;
    private OrderRepository orderRepository;
    private OrderDomain orderDomain;

    @Before
    public void setUp(){
        itemRepository = mock(ItemRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderDomain = new OrderDomain(itemRepository, orderRepository);
    }

    @Test
    public void makeOrder() throws Exception {

        List<Item> saved = Stream.of(new Item(1L, "banana", 5),
            new Item(2L, "apple", 5))
            .collect(Collectors.toList());
        List<OrderItem> orderItems = Stream.of(new OrderItem("banana", 5),
            new OrderItem("apple", 5))
            .collect(Collectors.toList());
        List<BuyItemDto> buyItems = Stream.of(new BuyItemDto("banana", 5),
            new BuyItemDto("apple", 5))
            .collect(Collectors.toList());
        given(itemRepository.findById(anyLong())).willReturn(Optional.of(saved.get(0)));

        Order expected = new Order();
        expected.setRealized(true);
        expected.setId(1L);
        expected.setItemList(orderItems);
        given(orderRepository.save(any(Order.class))).willReturn(expected);

        Order actual = orderDomain.makeOrder(buyItems);

        assertThat(actual, is(equalTo(expected)));
    }

}