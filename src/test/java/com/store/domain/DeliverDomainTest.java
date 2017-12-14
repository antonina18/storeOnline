package com.store.domain;

import com.store.dto.ItemDto;
import com.store.persistence.entities.Item;
import com.store.persistence.entities.Magazine;
import com.store.persistence.entities.Order;
import com.store.persistence.entities.OrderItem;
import com.store.persistence.repositories.ItemRepository;
import com.store.persistence.repositories.MagazineRepository;
import com.store.persistence.repositories.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

public class DeliverDomainTest {

    private IDeliverDomain deliverDomain;
    private MagazineRepository magazineRepository;
    private ItemRepository itemRepository;
    private OrderRepository orderRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        magazineRepository = mock(MagazineRepository.class);
        itemRepository = mock(ItemRepository.class);
        orderRepository = mock(OrderRepository.class);
        deliverDomain = new DeliverDomain(magazineRepository, itemRepository, orderRepository);
    }

    @Test
    public void addItem(){
        //given
        ItemDto item = new ItemDto("banana", 5);
        ItemDto expected = new ItemDto(1L, "banana", 5, false);
        Magazine magazine = new Magazine();
        magazine.setId(1L);
        Item saved = new Item(1L,  "banana", 5);
        given(magazineRepository.findById(anyLong())).willReturn(magazine);
        given(itemRepository.save(any(Item.class))).willReturn(saved);
        given(itemRepository.findByName(anyString())).willReturn(Optional.of(saved));

        //when
        ItemDto actual = deliverDomain.addItem(item, 1L);

        //then
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void realizeOrder(){
        //given
        List<Item> items = Stream.of(new Item(5, "banana"), new Item(5, "apple"))
            .collect(Collectors.toList());
        List<ItemDto> expected = Stream.of(new ItemDto(1L, "banana", 5, false),
            new ItemDto(2L, "apple", 5, false))
            .collect(Collectors.toList());
        List<Item> saved = Stream.of(new Item(1L, "banana", 5),
            new Item(2L, "apple", 5))
            .collect(Collectors.toList());
        List<OrderItem> buyItems = Stream.of(new OrderItem("banana", 5),
            new OrderItem("apple", 5))
            .collect(Collectors.toList());
        Magazine magazine = new Magazine();
        magazine.setId(1L);
        Order order = new Order();
        order.setItemList(buyItems);
        given(orderRepository.findById(anyLong())).willReturn(order);
        given(magazineRepository.findById(anyLong())).willReturn(magazine);
        given(itemRepository.findByName(anyString())).willReturn(Optional.of(saved.get(0)));
        given(itemRepository.save(items)).willReturn(saved);
        given(itemRepository.save(any(Item.class))).willReturn(saved.get(0), saved.get(1));

        //when
        List<ItemDto> actual = deliverDomain.realizeOrder(1L, 1L);

        //then
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void getItems(){
        //given
        Set<Item> items = Stream.of(new Item(1L,5, "banana", 5), new Item(2L, 5, "apple", 5))
            .collect(Collectors.toSet());
        List<ItemDto> expected = Stream.of(new ItemDto(1L, "banana", 5, false),
            new ItemDto(2L, "apple", 5, false))
            .collect(Collectors.toList());
        Magazine magazine = new Magazine();
        magazine.setId(1L);
        given(magazineRepository.findById(anyLong())).willReturn(magazine);
        magazine.setItems(items);

        //when
        List<ItemDto> actual = deliverDomain.getItems(1L);

        //then
        assertThat(actual, is(equalTo(expected)));
    }

}