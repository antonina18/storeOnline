package com.store.domain;

import com.store.dto.BuyItemDto;
import com.store.persistence.entities.Item;
import com.store.persistence.entities.Order;
import com.store.persistence.entities.OrderItem;
import com.store.persistence.repositories.ItemRepository;
import com.store.persistence.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class OrderDomain implements IOrderDomain {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    public OrderDomain(ItemRepository itemRepository, OrderRepository orderRepository) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    //// TODO: 20.11.17 nie działa
    @Override
    @Transactional
    public Order makeOrder(List<BuyItemDto> itemList) {
        Order order = new Order();
        List<OrderItem> orderItems = itemList.stream()
            .map(this::mapToOrderItem)
            .collect(Collectors.toList());
        order.setItemList(orderItems);
        order.setRealized(false);

        return orderRepository.save(order);
    }

    public OrderItem mapToOrderItem(BuyItemDto item) {
        return new OrderItem(item.getName(), item.getUnits());
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
}
