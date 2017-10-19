package com.store.domain;

import com.store.persistence.entities.Customer;
import com.store.utils.Response;
import com.store.utils.Token;

import java.util.Optional;

public interface IAuthenticationDomain {

    Response login(String name);

    Optional<Customer> getCustomer(Token token);

}
