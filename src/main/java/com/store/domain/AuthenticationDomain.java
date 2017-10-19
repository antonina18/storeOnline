package com.store.domain;

import com.store.persistence.entities.Customer;
import com.store.persistence.repositories.CustomerRepository;
import com.store.utils.Response;
import com.store.utils.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class AuthenticationDomain implements IAuthenticationDomain {

    private CustomerRepository customerRepository;
    private HashMap<Token, String> tokens;

    @Autowired
    public AuthenticationDomain(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.tokens = new HashMap<>();
    }

    @Override
    @Transactional
    public Response login(String name) {
        final Optional<Customer> customer = customerRepository.findByName(name);
        return customer.map(this::generateToken)
            .orElse(Response.create(HttpStatus.UNAUTHORIZED.name()));
    }

    private Response generateToken(Customer customer) {
        return Token.generateToken(customer)
            .map(token -> populateTokenStorage(token, customer.getName()))
            .orElse(Response.create("Error while generating token"));
    }

    private Response populateTokenStorage(Token token, String name) {
        tokens.put(token, name);
        return Response.create(token, Response.OK);
    }

    @Override
    @Transactional
    public Optional<Customer> getCustomer(Token token) {
        final String customerName = tokens.get(token);
        return customerRepository.findByName(customerName);
    }



}
