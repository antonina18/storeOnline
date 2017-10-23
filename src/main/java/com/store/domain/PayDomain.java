package com.store.domain;

import com.store.dto.BuyItemDto;
import com.store.persistence.entities.*;
import com.store.persistence.repositories.ItemRepository;
import com.store.persistence.repositories.PromotionItemsRepository;
import com.store.utils.Receipt;
import com.store.utils.Token;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class PayDomain implements IPayDomain {

    private final ItemRepository itemRepository;
    private final IAuthenticationDomain authenticationDomain;
    private final PromotionItemsRepository promotionItemsRepository;

    public PayDomain(ItemRepository itemRepository, IAuthenticationDomain authenticationDomain, PromotionItemsRepository promotionItemsRepository) {
        this.itemRepository = itemRepository;
        this.authenticationDomain = authenticationDomain;
        this.promotionItemsRepository = promotionItemsRepository;
    }

    @Override
    @Transactional
    public Receipt putItemToBasket(Token token, BuyItemDto buyItemDto) {
        Optional<Item> item = itemRepository.findByName(buyItemDto.getName());
        item.ifPresent(it -> getBasket(token).put(it, buyItemDto.getUnits()));
        return scanBasket(token);
    }

    @Override
    @Transactional
    public Receipt scanBasket(Token token) {
        Basket basket = getBasket(token);
        Integer finalPrice = calculatePrice(basket, token);
        Integer grantedRebate = basket.scanPrice() - finalPrice;
        return new Receipt(basket.getProducts(), basket.scanPrice(),
            grantedRebate, finalPrice);
    }

    @Override
    @Transactional
    public void clearBasket(Token token) {
        Optional<Customer> customer = authenticationDomain.getCustomer(token);
        customer.ifPresent(Customer::clearBasket);
    }

    //// TODO: 19.10.17  cos z tym null
    private Basket getBasket(Token token) {
        return authenticationDomain.getCustomer(token)
            .map(Customer::getBasket)
            .orElse(null);
    }

    private Integer calculatePrice(Basket basket, Token token) {
        boolean isSpecial = false;
        Integer specialPrice = countPriceWithSpecialPrice(basket);
        Integer promotionItemsRebate = countPriceWithPromotionItems(basket, token);
        if (specialPrice > 0 && specialPrice < promotionItemsRebate) {
            isSpecial = true;
        }
        return isSpecial ? specialPrice : promotionItemsRebate;
    }


    private Integer countPriceWithSpecialPrice(Basket basket) {
        return basket.getContent()
            .stream()
            .mapToInt(this::countForSpecialPrice).sum();

    }

    private int countForSpecialPrice(BasketItem basketItem) {
        Integer amountForSpecialPrice = 0;
        Integer priceWithSpecialPrice = 0;
        Integer specialPriceAmount = 0;
        SpecialPrice specialPrice = basketItem.getItem().getSpecialPrice();
        if (specialPrice != null) {
            amountForSpecialPrice = specialPrice.getUnit();
            priceWithSpecialPrice = specialPrice.getPrice();
            specialPriceAmount = basketItem.getUnits() / amountForSpecialPrice;
        }
        Integer normalPriceAmount = basketItem.getUnits() - (specialPriceAmount * amountForSpecialPrice);
        return priceWithSpecialPrice * specialPriceAmount + normalPriceAmount * basketItem.getItem().getPrice();
    }

    private Integer countPriceWithPromotionItems(Basket basket, Token token) {
        List<PromotionItems> promotionItems = promotionItemsRepository.findAll();

        Integer rebate = authenticationDomain.getCustomer(token)
            .map(customer -> customer.rebatePromotionItems(basket, promotionItems))
            .orElse(0);
        int price = basket.getContent()
            .stream()
            .mapToInt(basketItem -> basketItem.getItem().getPrice() * basketItem.getUnits())
            .sum();

        return price - rebate;

    }
}
