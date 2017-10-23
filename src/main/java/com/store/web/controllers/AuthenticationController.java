package com.store.web.controllers;

import com.store.domain.IAuthenticationDomain;
import com.store.dto.AuthDto;
import com.store.utils.Token;
import com.store.utils.TokenException;
import com.store.utils.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/")
public class AuthenticationController {


    private IAuthenticationDomain authDomain;

    public AuthenticationController(IAuthenticationDomain authDomain) {
        this.authDomain = authDomain;
    }

    @PostMapping(value = "login")
    public ResponseEntity<Token> login(@Valid AuthDto auth) {
        Token token;
        try {
            token = authDomain.login(auth.getName());
        } catch (TokenException | UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(token, HttpStatus.OK);

    }
}
