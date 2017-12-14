package com.store.domain;

import com.store.dto.BuyItemDto;
import com.store.persistence.entities.*;
import com.store.persistence.repositories.ItemRepository;
import com.store.persistence.repositories.MagazineRepository;
import com.store.persistence.repositories.PromotionItemsRepository;
import com.store.persistence.repositories.SpecialPriceRepository;
import com.store.utils.Product;
import com.store.utils.Receipt;
import com.store.utils.Token;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class PayDomain implements IPayDomain {

    private final IAuthenticationDomain authenticationDomain;
    private final PromotionItemsRepository promotionItemsRepository;
    private final MagazineRepository magazineRepository;
    private final ItemRepository itemRepository;
    private final SpecialPriceRepository specialPriceRepository;

    public PayDomain(IAuthenticationDomain authenticationDomain, PromotionItemsRepository promotionItemsRepository, MagazineRepository magazineRepository, ItemRepository itemRepository, SpecialPriceRepository specialPriceRepository) {
        this.authenticationDomain = authenticationDomain;
        this.promotionItemsRepository = promotionItemsRepository;
        this.magazineRepository = magazineRepository;
        this.itemRepository = itemRepository;
        this.specialPriceRepository = specialPriceRepository;
    }

    @Override
    @Transactional
    public Receipt putItemToBasket(Token token, BuyItemDto buyItemDto) {
        Optional<Item> item = itemRepository.findByName(buyItemDto.getName());
        item.ifPresent(it -> {
            if(it.getUnit() >= buyItemDto.getUnits())
            getBasket(token).put(it, buyItemDto.getUnits());
            removeItemsFromMagazine(token, buyItemDto.getUnits());
        });
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

    private void removeItemsFromMagazine(Token token, Integer units) {
        Basket basket = getBasket(token);
        basket.getProducts()
            .stream()
            .map(Product::getName)
            .forEach(name -> {
                findItemAndRemove(units, name);
            });
    }

    private void findItemAndRemove(Integer units, String name) {
        Optional<Item> item = itemRepository.findByName(name);
        item.ifPresent(it -> {
            Magazine magazine = getMagazine();
            magazine.getItems()
                .forEach(i-> removeItem(units, i));
        });
    }

    private void removeItem(Integer units, Item i) {
        if (!i.getUnit().equals(0) && i.getUnit() > units) {
            i.setUnit(i.getUnit() - units);
        }
    }

    private Magazine getMagazine() {
        return magazineRepository.findById(1L);
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
        Integer specialPriceId = basketItem.getItem().getSpecialPriceId();
        Optional<SpecialPrice> specialPrice = specialPriceRepository.findById(specialPriceId);

        if (specialPrice.isPresent()) {
            amountForSpecialPrice = specialPrice.get().getUnit();
            priceWithSpecialPrice = specialPrice.get().getPrice();
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
