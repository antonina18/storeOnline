package com.store.web.controllers;


import com.store.domain.IDeliverDomain;
import com.store.domain.IPayDomain;
import com.store.dto.BuyItemDto;
import com.store.dto.ItemDto;
import com.store.persistence.entities.Item;
import com.store.utils.Receipt;
import com.store.utils.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

/**
 * Class to administrate buying.
 */
@RestController
@RequestMapping(value = "/deliver")
public class SupplierController {

    private final IDeliverDomain deliverDomain;

    @Autowired
    public SupplierController(IDeliverDomain deliverDomain) {
        this.deliverDomain = deliverDomain;
    }

    @PostMapping("/item")
    public ResponseEntity<ItemDto> addItem(@RequestBody ItemDto item, @RequestParam Long magazineId) {
        ItemDto itemDto = deliverDomain.addItem(item, magazineId);
        return new ResponseEntity<>(itemDto, HttpStatus.OK);
    }

    @PostMapping(value = "/items")
    public ResponseEntity<List<ItemDto>> realizeOrder(@RequestParam Long orderId,
                                                      @RequestParam Long magazineId) {
        List<ItemDto> response = deliverDomain.realizeOrder(orderId, magazineId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAll(@RequestParam Long magazineId) {
        List<ItemDto> items = deliverDomain.getItems(magazineId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

}
