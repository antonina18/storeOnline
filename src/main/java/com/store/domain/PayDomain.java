package com.store.domain;

import com.store.persistence.repositories.ItemRepository;
import com.store.utils.Receipt;
import com.store.utils.Response;
import com.store.utils.Token;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PayDomain implements IPayDomain {

    private final ItemRepository itemRepository;
    private final IAuthenticationDomain authenticationDomain;

    public PayDomain(ItemRepository itemRepository, IAuthenticationDomain authenticationDomain) {
        this.itemRepository = itemRepository;
        this.authenticationDomain = authenticationDomain;
    }

    @Override
    @Transactional
    public Response putItemToBasket(Token token, String name, int amount) {
        return null;
    }

    @Override
    @Transactional
    public Receipt scanBasket(Token token) {
        return null;
    }

    @Override
    @Transactional
    public void clearBasket(Token token) {

    }

}
