package com.store;

import com.store.domain.IAuthenticationDomain;
import com.store.persistence.entities.*;
import com.store.persistence.repositories.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

@Component
public class DataLoader {

    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final IAuthenticationDomain authenticationDomain;
    private final ConfigurableApplicationContext context;
    private final MagazineRepository magazineRepository;
    private final PromotionItemsRepository promotionItemsRepository;
    private final SpecialPriceRepository specialPriceRepository;

    public DataLoader(CustomerRepository customerRepository, ItemRepository itemRepository, IAuthenticationDomain authenticationDomain, ConfigurableApplicationContext context, MagazineRepository magazineRepository, PromotionItemsRepository promotionItemsRepository, SpecialPriceRepository specialPriceRepository) {
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.authenticationDomain = authenticationDomain;
        this.context = context;
        this.magazineRepository = magazineRepository;
        this.promotionItemsRepository = promotionItemsRepository;
        this.specialPriceRepository = specialPriceRepository;
    }

    @PostConstruct
    public void init() {
        createUsers();
//        createItemsAndPromotions();
        createMagazines();
        createPromotionItems();
    }

    private void createPromotionItems() {
        promotionItemsRepository.save(new PromotionItems("apple", "banana", 3));
        promotionItemsRepository.save(new PromotionItems("dill", "banana", 5));
        promotionItemsRepository.save(new PromotionItems("apple", "cherry", 2));
    }

    private void createMagazines() {
        magazineRepository.save(new Magazine(1L, "store", createItems()));
    }

    private void createUsers() {
        customerRepository.save(new Customer("ADMIN"));
        customerRepository.save(new Customer("BOGUSŁAW"));
        customerRepository.save(new Customer("CZESŁAW"));
    }


    private Set<Item> createItems() {
        Item apple = new Item(1L, 5, "apple", 5);
        SpecialPrice specialPrice = new SpecialPrice(5, 10);
        SpecialPrice saved = specialPriceRepository.save(specialPrice);
        apple.setSpecialPriceId(saved.getId());
        Item banana = new Item(2L, 2, "banana", 5);
        Item cherry = new Item(3L, 100, "cherry", 5);
        SpecialPrice specialPriceCh = new SpecialPrice(2, 80);
        SpecialPrice savedCh = specialPriceRepository.save(specialPriceCh);
        cherry.setSpecialPriceId(savedCh.getId());
        Item dill = new Item(4L, 10, "dill", 5);

        Set<Item> items = Stream.of(apple, banana, cherry, dill)
            .collect(Collectors.toSet());

        return new HashSet<>(itemRepository.save(items));

    }
}
