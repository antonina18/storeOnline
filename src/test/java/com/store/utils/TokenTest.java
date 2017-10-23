package com.store.utils;

import com.store.persistence.entities.Customer;
import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class TokenTest {

    private static final String TOKEN_VALUE = "TOKEN_VALUE";
    private Customer customer;
    private Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    @Before
    public void setUp() throws Exception {
        customer = new Customer(TOKEN_VALUE);
    }

    @Test
    public void testGenerateToken() throws Exception {
        //given
        Token.clock = clock;

        //when
        final Optional<Token> actual = Token.generateToken(customer);

        //then
        assertTrue(actual.isPresent());
        assertThat(actual.get().getValue(), is(equalTo(MD5(TOKEN_VALUE))));
    }

    private String MD5(String string) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update((string + LocalDateTime.now(clock)).getBytes());
        return new String(messageDigest.digest()).toUpperCase();
    }

}