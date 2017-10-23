package com.store.web.controllers;


import com.store.domain.IPayDomain;
import com.store.dto.BuyItemDto;
import com.store.utils.Receipt;
import com.store.utils.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Class to administrate buying.
 */
@RestController
@RequestMapping(value = "/buy")
public class BuyController {

    private final IPayDomain payDomain;

    @Autowired
    public BuyController(IPayDomain payDomain) {
        this.payDomain = payDomain;
    }

    @RequestMapping(method = RequestMethod.POST, value = "item/add")
    public ResponseEntity<Receipt> addItemToBasket(@Valid BuyItemDto buyItemDto,
                                      @RequestHeader("Authorization") final String auth) {
        Receipt receipt = payDomain.putItemToBasket(new Token(auth), buyItemDto);
        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }

    @GetMapping("/pay")
    public ResponseEntity<Receipt> pay(@RequestHeader("Authorization") final String auth) {
        Receipt receipt = payDomain.scanBasket(new Token(auth));
        payDomain.clearBasket(new Token(auth));
        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }

}
