package com.store.domain;

import com.store.persistence.entities.Customer;
import com.store.persistence.repositories.CustomerRepository;
import com.store.utils.Token;
import com.store.utils.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Token login(String name) throws UnauthorizedException {
        final Optional<Customer> customer = customerRepository.findByName(name);
        return customer.map(this::generateToken)
            .filter(token -> token.getValue() != null || !token.getValue().isEmpty())
            .orElseThrow(UnauthorizedException::new);
    }

    private Token generateToken(Customer customer) {
        return Token.generateToken(customer)
            .map(token -> populateTokenStorage(token, customer.getName()))
            .orElse(new Token());
    }

    private Token populateTokenStorage(Token token, String name) {
        tokens.put(token, name);
        return token;
    }

    @Override
    @Transactional
    public Optional<Customer> getCustomer(Token token) {
        final String customerName = tokens.get(token);
        return customerRepository.findByName(customerName);
    }

    @Override
    public boolean containsToken(Token token) {
        return token != null && token.getValue() != null && tokens.containsKey(token);
    }

}
