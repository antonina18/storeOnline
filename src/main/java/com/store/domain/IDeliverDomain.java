package com.store.domain;

import com.store.dto.ItemDto;

import java.util.List;

public interface IDeliverDomain {

    List<ItemDto> realizeOrder(Long orderId, Long magazineId);

    List<ItemDto> getItems(Long magazineId);

    ItemDto addItem(ItemDto item, Long magazineId);
}
