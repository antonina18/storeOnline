package com.store.domain;

import com.store.persistence.entities.*;
import com.store.persistence.repositories.ItemRepository;
import com.store.persistence.repositories.MagazineRepository;
import com.store.persistence.repositories.PromotionItemsRepository;
import com.store.persistence.repositories.SpecialPriceRepository;
import com.store.utils.Product;
import com.store.utils.Receipt;
import com.store.utils.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.*;
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
    private AuthenticationDomain authenticationDomain;
    private PromotionItemsRepository promotionItemsRepository;
    private MagazineRepository magazineRepository;
    private ItemRepository itemRepository;
    private SpecialPriceRepository specialPriceRepository;

    private Token token;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        magazineRepository = mock(MagazineRepository.class);
        authenticationDomain = mock(AuthenticationDomain.class);
        promotionItemsRepository = mock(PromotionItemsRepository.class);
        itemRepository = mock(ItemRepository.class);
        specialPriceRepository = mock(SpecialPriceRepository.class);
        payDomain = new PayDomain(authenticationDomain, promotionItemsRepository, magazineRepository, itemRepository, specialPriceRepository);
        token = new Token("ADMIN");
        customer = new Customer("ADMIN");
        when(authenticationDomain.getCustomer(any())).thenReturn(Optional.of(customer));
    }

    @Test
    public void putItemToBasket() throws Exception {
        //given
        Receipt expected = new Receipt(createListWithOneProduct(),25, 0, 25);

        Item apple = item("apple", 5);
        when(magazineRepository.findItemByName(eq("apple"))).thenReturn(Optional.of(apple));

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
        when(magazineRepository.findItemByName(eq("apple"))).thenReturn(Optional.of(apple));
        SpecialPrice specialPrice = new SpecialPrice(1, 3, 3);
        apple.setSpecialPriceId(specialPrice.getId());

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
        when(magazineRepository.findItemByName(eq("banana"))).thenReturn(Optional.of(banana));

        //when
        payDomain.putItemToBasket(token, buyItemDto("banana", 5));

        Item bread = item("bread", 5);
        when(magazineRepository.findItemByName(eq("bread"))).thenReturn(Optional.of(bread));
        Receipt actual = payDomain.putItemToBasket(token, buyItemDto("bread", 3));

        //then
        assertThat(actual.getContent(), containsInAnyOrder(expected.getContent().toArray()));
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
        SpecialPrice specialPrice = new SpecialPrice(2,3, 3);
        banana.setSpecialPriceId(specialPrice.getId());
        when(magazineRepository.findItemByName(eq("banana"))).thenReturn(Optional.of(banana));

        //when
        payDomain.putItemToBasket(token, buyItemDto("banana", 5));

        Item bread = item("bread", 5);
        when(magazineRepository.findItemByName(eq("bread"))).thenReturn(Optional.of(bread));
        Receipt actual = payDomain.putItemToBasket(token, buyItemDto("bread", 3));

        //then
        assertThat(actual.getContent(), containsInAnyOrder(expected.getContent().toArray()));
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
        SpecialPrice specialPrice = new SpecialPrice(3,3, 300);
        banana.setSpecialPriceId(specialPrice.getId());
        when(magazineRepository.findItemByName(eq("banana"))).thenReturn(Optional.of(banana));

        //when
        payDomain.putItemToBasket(token, buyItemDto("banana", 5));

        Item bread = item("bread", 5);
        when(magazineRepository.findItemByName(eq("bread"))).thenReturn(Optional.of(bread));
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
        Item apple = new Item(2, "apple");
        BasketItem basketItem = new BasketItem(apple, 1);
        Set<BasketItem> content = new HashSet<>();
        content.add(basketItem);
        customer.getBasket().setContent(content);
        when(magazineRepository.findItemByName(eq("apple"))).thenReturn(Optional.of(apple));

        //when
        payDomain.clearBasket(token);

        //then
        assertTrue(customer.getBasket().getContent().isEmpty());
    }

    // TODO: 30.10.17 zrobic test na usuwanie zawartosci i dodawanie


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