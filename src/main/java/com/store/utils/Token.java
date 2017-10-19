package com.store.utils;

import com.store.persistence.entities.Customer;
import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.xml.bind.DatatypeConverter;

import static org.springframework.util.Assert.notNull;

@Data
public class Token {

    static Clock clock = Clock.systemDefaultZone();
    private String value;

    public Token(String value) {
        this.value = value;
    }

    public static Optional<Token> generateToken(Customer customer) {
        notNull(customer, "Can't generate token without data");
        Token token = null;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update((customer.getName() + LocalDateTime.now(clock)).getBytes());
            token = createToken(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(token);
    }

    private static Token createToken(MessageDigest messageDigest) {
        final String encryptedString = DatatypeConverter.printHexBinary(messageDigest.digest()).toUpperCase();
        return new Token(encryptedString);
    }


}
