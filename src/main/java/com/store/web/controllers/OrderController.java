package com.store.web.controllers;

import com.store.domain.IOrderDomain;
import com.store.dto.BuyItemDto;
import com.store.dto.ItemDto;
import com.store.persistence.entities.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/order")
public class OrderController {


    private IOrderDomain orderDomain;

    public OrderController(IOrderDomain orderDomain) {
        this.orderDomain = orderDomain;
    }

    //// TODO: 20.11.17 add authorization
    @PostMapping
    public ResponseEntity<Order> makeOrder(@RequestBody List<BuyItemDto> itemList) {
        Order order = orderDomain.makeOrder(itemList);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderDomain.getOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
