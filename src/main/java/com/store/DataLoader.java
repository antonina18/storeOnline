package com.store;

import com.store.domain.IAuthenticationDomain;
import com.store.persistence.entities.Customer;
import com.store.persistence.entities.Item;
import com.store.persistence.entities.SpecialPrice;
import com.store.persistence.repositories.CustomerRepository;
import com.store.persistence.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataLoader {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private IAuthenticationDomain authenticationDomain;

    @Autowired
    private ConfigurableApplicationContext context;

    @PostConstruct
    public void init() {
        createUsers();
        createItemsAnPromotions();
    }

    private void createUsers() {
        customerRepository.save(new Customer("ADMIN"));
        customerRepository.save(new Customer("BOGUSŁAW"));
        customerRepository.save(new Customer("CZESŁAW"));
    }

    private void createItemsAnPromotions() {
        Item apple = new Item(5, "APPLE");
        apple.setPromotion(new SpecialPrice(5, 10));
        Item banana = new Item(2, "BANANA");
        Item cherry = new Item(100, "CHERRY");
        cherry.setPromotion(new SpecialPrice(2, 80));
        Item dill = new Item(10, "DILL");

        itemRepository.save(apple);
        itemRepository.save(banana);
        itemRepository.save(cherry);
        itemRepository.save(dill);

    }
}
