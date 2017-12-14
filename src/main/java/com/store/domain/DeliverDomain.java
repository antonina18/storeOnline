package com.store.domain;

import com.store.dto.ItemDto;
import com.store.persistence.entities.Item;
import com.store.persistence.entities.Magazine;
import com.store.persistence.entities.Order;
import com.store.persistence.repositories.ItemRepository;
import com.store.persistence.repositories.MagazineRepository;
import com.store.persistence.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class DeliverDomain implements IDeliverDomain {

    private final MagazineRepository magazineRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    public DeliverDomain(MagazineRepository magazineRepository, ItemRepository itemRepository, OrderRepository orderRepository) {
        this.magazineRepository = magazineRepository;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public List<ItemDto> realizeOrder(Long orderId, Long magazineId) {
        List<Item> items = new ArrayList<>();
        Magazine magazine = magazineRepository.findById(magazineId);
        Order order = orderRepository.findById(orderId);
        order.getItemList()
            .forEach(item-> {
                final String name = item.getName();
                Optional<Item> oItem = itemRepository.findByName(name);
                oItem.ifPresent(it -> magazine.getItems()
                    .stream()
                    .filter(e -> e.getName().equals(it.getName()))
                    .forEach(e -> {
                        e.setUnit(e.getUnit() + item.getUnits());
                        items.add(e);
                    }));
            });
        order.setRealized(true);

        return items.stream()
            .map(this::buildItem)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ItemDto> getItems(Long magazineId) {
        Magazine magazine = magazineRepository.findById(magazineId);
        return magazine.getItems()
            .stream()
            .map(this::buildItem)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDto addItem(ItemDto item, Long magazineId) {
        Optional<Item> oItem = itemRepository.findByName(item.getName());
        Magazine magazine = magazineRepository.findById(magazineId);
        if (oItem.isPresent()) {
            Item it = oItem.get();
            magazine.getItems()
                .stream()
                .filter(e -> e.getName().equals(it.getName()))
                .forEach(e -> e.setUnit(e.getUnit() + 1));
            return buildItem(it);
        } else {
            Item saved = itemRepository.save(new Item(item.getPrice(), item.getName()));
            magazine.getItems()
                .add(saved);
            return buildItem(saved);
        }
    }

    private ItemDto buildItem(Item item) {
        ItemDto itemDto = ItemDto.builder()
            .id(item.getId())
            .name(item.getName())
            .price(item.getPrice())
            .units(item.getUnit())
            .build();

        if (item.getSpecialPriceId() != null) {
            itemDto.setSpecialPrice(true);
        }
        return itemDto;
    }
}
