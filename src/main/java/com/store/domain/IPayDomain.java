package com.store.domain;

import com.store.dto.BuyItemDto;
import com.store.utils.Receipt;
import com.store.utils.Token;

public interface IPayDomain {

    Receipt putItemToBasket(Token token, BuyItemDto buyItemDto);

    Receipt scanBasket(Token token);

    void clearBasket(Token token);
}
