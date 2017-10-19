package com.store.domain;

import com.store.utils.Receipt;
import com.store.utils.Response;
import com.store.utils.Token;

public interface IPayDomain {

    Response putItemToBasket(Token token, String name, int amount);

    Receipt scanBasket(Token token);

    void clearBasket(Token token);
}
