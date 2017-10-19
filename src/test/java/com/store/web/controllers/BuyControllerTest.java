package com.store.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.store.Application;
import com.store.domain.IPayDomain;
import com.store.utils.Receipt;
import com.store.utils.Token;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class BuyControllerTest {

    private ConfigurableApplicationContext context;
    private BuyController buyController;
    private AuthenticationController authenticationController;
    private MockMvc mockMvc;
    private String token;
    private ObjectMapper mapper;


    public BuyControllerTest(ConfigurableApplicationContext context, BuyController buyController, AuthenticationController authenticationController, MockMvc mockMvc, String token) {
        this.context = context;
        this.buyController = buyController;
        this.authenticationController = authenticationController;
        this.mockMvc = mockMvc;
        this.token = token;
        this.mapper = new ObjectMapper();
    }

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
            .standaloneSetup(buyController)
            .build();
        generateToken();
    }

    @Ignore
    @Test
    public void testScan() throws Exception {
        generateBasket();
        MvcResult mvcResult = mockMvc
            .perform(get("/storeOnline/buy/scan")
                .header("Authorization", token))
            .andReturn();
        validateReceipt(mvcResult);
    }

    @Ignore
    @Test
    public void testPay() throws Exception {
        generateBasket();
        MvcResult payResult = mockMvc
            .perform(post("/storeOnline/buy/pay")
                .header("Authorization", token))
            .andReturn();
        validateReceipt(payResult);

        MvcResult scanResult = mockMvc
            .perform(get("/storeOnline/buy/scan")
                .header("Authorization", token))
            .andReturn();
        validateReceiptIsEmpty(scanResult);
    }

    private void validateReceipt(MvcResult mvcResult) throws IOException {
        //given
        String json = mvcResult.getResponse().getContentAsString();

        //when
        final Receipt receipt = mapper.readValue(json, Receipt.class);

        //then
        assertNotNull(receipt);
        receipt.getContent().forEach(product -> {
            assertNotNull(product.getPriceWithoutPromotion());
            assertNotNull(product.getFinalPrice());
            assertNotNull(product.getAmount());
            assertNotNull(product.getPromotionRefund());
            assertNotNull(product.getName());
        });
    }

    private void validateReceiptIsEmpty(MvcResult mvcResult) throws IOException {
        //given
        String json = mvcResult.getResponse().getContentAsString();

        //when
        final Receipt receipt = mapper.readValue(json, Receipt.class);

        //then
        assertNotNull(receipt);
        assertTrue(receipt.getContent().isEmpty());
    }

    private void generateToken() throws Exception {
        MockMvc tokenMVC = MockMvcBuilders
            .standaloneSetup(authenticationController)
            .build();
        MvcResult mvcResult = tokenMVC
            .perform(post("/login")
                .param("name", "ADMIN"))
            .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        final ObjectNode node = mapper.readValue(json, ObjectNode.class);
        token = node.get("token").asText();
    }

    private void generateBasket() throws Exception {
        IPayDomain payDomain = context.getBean(IPayDomain.class);
        payDomain.putItemToBasket(new Token(token), "APPLE", 10);
        payDomain.putItemToBasket(new Token(token), "BANANA", 10);
        payDomain.putItemToBasket(new Token(token), "CHERRY", 10);
    }


}