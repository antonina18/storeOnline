package com.store.web.controllers;


import com.store.domain.IPayDomain;
import com.store.utils.Receipt;
import com.store.utils.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Class to administrate buying.
 */
@RestController
@RequestMapping(value = "/buy")
public class BuyController {

    private final IPayDomain payDomain;

    public BuyController(IPayDomain payDomain) {
        this.payDomain = payDomain;
    }

    @GetMapping("/pay")
    public ResponseEntity<Receipt> pay(@RequestHeader("Authorization") final String auth) {
        Receipt receipt = payDomain.scanBasket(new Token(auth));
        payDomain.clearBasket(new Token(auth));
        return new ResponseEntity<>(receipt, HttpStatus.OK);
    }

    @GetMapping("/scan")
    public ResponseEntity<Receipt> scan(@RequestHeader("Authorization") final String auth) {
        Receipt receipt = payDomain.scanBasket(new Token(auth));
        return new ResponseEntity<>(receipt, HttpStatus.OK);

    }

}
