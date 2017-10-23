package com.store.domain;

import com.store.persistence.entities.Customer;
import com.store.persistence.entities.Item;
import com.store.persistence.entities.PromotionItems;
import com.store.persistence.entities.SpecialPrice;
import com.store.persistence.repositories.ItemRepository;
import com.store.persistence.repositories.PromotionItemsRepository;
import com.store.utils.Product;
import com.store.utils.Receipt;
import com.store.utils.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.store.stub.ItemObjectMother.buyItemDto;
import static com.store.stub.ItemObjectMother.item;
import static com.store.stub.PromotionItemsObjectMother.promotionItems;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PayDomainTest {

    private PayDomain payDomain;
    private ItemRepository itemRepository;
    private AuthenticationDomain authenticationDomain;
    private PromotionItemsRepository promotionItemsRepository;

    private Token token;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        itemRepository = mock(ItemRepository.class);
        authenticationDomain = mock(AuthenticationDomain.class);
        promotionItemsRepository = mock(PromotionItemsRepository.class);
        payDomain = new PayDomain(itemRepository, authenticationDomain, promotionItemsRepository);
        token = new Token("ADMIN");
        customer = new Customer("ADMIN");
        when(authenticationDomain.getCustomer(any())).thenReturn(Optional.of(customer));
    }

    @Test
    public void putItemToBasket() throws Exception {
        //given
        Receipt expected = new Receipt(createListWithOneProduct(),25, 0, 25);

        Item apple = item("apple", 5);
        when(itemRepository.findByName(eq("apple"))).thenReturn(Optional.of(apple));

        //when
        Receipt actual = payDomain.putItemToBasket(token, buyItemDto("apple", 5));

        //then
        assertTrue(!customer.getBasket().getContent().isEmpty());
        assertThat(actual.getContent(), is(equalTo(expected.getContent())));
    }

    @Test
    public void putItemToBasketWithSpecialPriceItem() throws Exception {
        //given
        Item apple = item("apple", 5);
        when(itemRepository.findByName(eq("apple"))).thenReturn(Optional.of(apple));
        SpecialPrice specialPrice = new SpecialPrice(3, 3);
        apple.setSpecialPrice(specialPrice);

        Receipt expected = new Receipt(createListWithOneProduct(), 25, 12, 13);

        //when
        Receipt actual = payDomain.putItemToBasket(token, buyItemDto("apple", 5));

        //then
        assertThat(actual.getContent(), is(equalTo(expected.getContent())));
        assertThat(actual.getBasePrice(), is(equalTo(expected.getBasePrice())));
        assertThat(actual.getRebateGranted(), is(equalTo(expected.getRebateGranted())));
        assertThat(actual.getFinalPrice(), is(equalTo(expected.getFinalPrice())));
    }

    @Test
    public void putItemToBasketWithPromotionItems() throws Exception {
        //given
        given(promotionItemsRepository.findAll()).willReturn(createPromotionItems());
        Receipt expected = new Receipt(createListWithMoreProducts(), 40, 1, 39);
        Item banana = item("banana", 5);
        when(itemRepository.findByName(eq("banana"))).thenReturn(Optional.of(banana));

        //when
        payDomain.putItemToBasket(token, buyItemDto("banana", 5));

        Item bread = item("bread", 5);
        when(itemRepository.findByName(eq("bread"))).thenReturn(Optional.of(bread));
        Receipt actual = payDomain.putItemToBasket(token, buyItemDto("bread", 3));

        //then
        assertThat(actual.getContent(), is(equalTo(expected.getContent())));
        assertThat(actual.getBasePrice(), is(equalTo(expected.getBasePrice())));
        assertThat(actual.getRebateGranted(), is(equalTo(expected.getRebateGranted())));
        assertThat(actual.getFinalPrice(), is(equalTo(expected.getFinalPrice())));
    }

    @Test
    public void putItemToBasketWithBothPromotionShouldReturnSpecialPrice() throws Exception {
        //given
        given(promotionItemsRepository.findAll()).willReturn(createPromotionItems());
        Receipt expected = new Receipt(createListWithMoreProducts(), 40, 12, 28);
        Item banana = item("banana", 5);
        SpecialPrice specialPrice = new SpecialPrice(3, 3);
        banana.setSpecialPrice(specialPrice);
        when(itemRepository.findByName(eq("banana"))).thenReturn(Optional.of(banana));

        //when
        payDomain.putItemToBasket(token, buyItemDto("banana", 5));

        Item bread = item("bread", 5);
        when(itemRepository.findByName(eq("bread"))).thenReturn(Optional.of(bread));
        Receipt actual = payDomain.putItemToBasket(token, buyItemDto("bread", 3));

        //then
        assertThat(actual.getContent(), is(equalTo(expected.getContent())));
        assertThat(actual.getBasePrice(), is(equalTo(expected.getBasePrice())));
        assertThat(actual.getRebateGranted(), is(equalTo(expected.getRebateGranted())));
        assertThat(actual.getFinalPrice(), is(equalTo(expected.getFinalPrice())));
    }

    @Test
    public void putItemToBasketWithBothPromotionsShouldChoosePromotionItems() throws Exception {
        //given
        given(promotionItemsRepository.findAll()).willReturn(createPromotionItems());
        Receipt expected = new Receipt(createListWithMoreProducts(), 40, 1, 39);
        Item banana = item("banana", 5);
        SpecialPrice specialPrice = new SpecialPrice(3, 300);
        banana.setSpecialPrice(specialPrice);
        when(itemRepository.findByName(eq("banana"))).thenReturn(Optional.of(banana));

        //when
        payDomain.putItemToBasket(token, buyItemDto("banana", 5));

        Item bread = item("bread", 5);
        when(itemRepository.findByName(eq("bread"))).thenReturn(Optional.of(bread));
        Receipt actual = payDomain.putItemToBasket(token, buyItemDto("bread", 3));

        //then
        assertThat(actual.getContent(), is(containsInAnyOrder(expected.getContent().toArray())));
        assertThat(actual.getBasePrice(), is(equalTo(expected.getBasePrice())));
        assertThat(actual.getRebateGranted(), is(equalTo(expected.getRebateGranted())));
        assertThat(actual.getFinalPrice(), is(equalTo(expected.getFinalPrice())));
    }

    @Test
    public void testClearBasket() throws Exception {
        //given
        customer.getBasket().put(new Item(), 5);

        //when
        payDomain.clearBasket(token);

        //then
        assertTrue(customer.getBasket().getContent().isEmpty());
    }


    private List<PromotionItems> createPromotionItems() {
        PromotionItems promotionItem = promotionItems("banana", "cherry", 3);
        PromotionItems promotionItem1 = promotionItems("bread", "cherry", 2);
        PromotionItems promotionItem2 = promotionItems("banana", "bread", 1);
        List<PromotionItems> promotionsItems = new ArrayList<>();
        promotionsItems.add(promotionItem);
        promotionsItems.add(promotionItem1);
        promotionsItems.add(promotionItem2);
        return promotionsItems;

    }

    private List<Product> createListWithOneProduct() {
        return Collections.singletonList(new Product("apple", 5, 25));
    }

    private List<Product> createListWithMoreProducts() {
        return Stream.of(new Product("bread", 3, 15),
            new Product("banana", 5, 25))
            .collect(Collectors.toList());
    }
}