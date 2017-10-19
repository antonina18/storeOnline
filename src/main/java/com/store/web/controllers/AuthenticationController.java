package com.store.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.store.domain.IAuthenticationDomain;
import com.store.dto.AuthDto;
import com.store.utils.Response;
import com.store.utils.Token;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/")
public class AuthenticationController {


    private IAuthenticationDomain authDomain;
    private ObjectMapper mapper;

    public AuthenticationController(IAuthenticationDomain authDomain, ObjectMapper mapper) {
        this.authDomain = authDomain;
        this.mapper = mapper;
    }

    @PostMapping(value = "login")
    public ObjectNode login(@Valid AuthDto auth, Errors errors) {
        final ObjectNode jsonObject = mapper.createObjectNode();
        if (errors.hasErrors()) {
            return jsonObject.put("status", errors.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(",")));
        }
        Response<Token> response = authDomain.login(auth.getName());
        jsonObject.put("status", response.getMessage());
        if (response.getData() != null) {
            jsonObject.put("token", response.getData().getValue());
        }
        return jsonObject;
    }
}
