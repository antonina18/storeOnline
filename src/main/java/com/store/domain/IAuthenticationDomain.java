package com.store.domain;

import com.store.persistence.entities.Customer;
import com.store.utils.Token;
import com.store.utils.TokenException;
import com.store.utils.UnauthorizedException;

import java.util.Optional;

public interface IAuthenticationDomain {

    Token login(String name) throws UnauthorizedException, TokenException;

    Optional<Customer> getCustomer(Token token);

    boolean containsToken(Token token);


}
