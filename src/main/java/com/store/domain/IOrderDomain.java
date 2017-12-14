package com.store.domain;

import com.store.dto.BuyItemDto;
import com.store.persistence.entities.Order;

import java.util.List;

public interface IOrderDomain {

    Order makeOrder(List<BuyItemDto> itemList);

    List<Order> getOrders();
}
