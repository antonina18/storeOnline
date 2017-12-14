//package com.store.web.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.store.utils.Receipt;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.io.IOException;
//
//import static org.junit.Assert.assertNotNull;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BuyControllerTest {
//
//    @Autowired
//    private ConfigurableApplicationContext context;
//
//    @Autowired
//    private WebApplicationContext wac;
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private BuyController buyController;
//
//    @Autowired
//    private AuthenticationController authenticationController;
//
//    private String token;
//    private ObjectMapper mapper = new ObjectMapper();
//
//    @Before
//    public void setUp() throws Exception {
//        mockMvc = webAppContextSetup(wac).build();
//        generateToken();
//    }
//
//    @Test
//    public void addItemToBasket() throws Exception {
//        MvcResult mvcResult = mockMvc
//            .perform(
//                post("/buy/item/add")
//                    .param("name", "apple")
//                    .param("units", "10")
//                    .header("Authorization", token))
//            .andReturn();
//
//        validateReceipt(mvcResult);
//    }
//
//    @Test
//    public void pay() throws Exception {
//
//        mockMvc
//            .perform(
//                post("/buy/item/add")
//                    .param("name", "apple")
//                    .param("units", "10")
//                    .header("Authorization", token))
//            .andExpect(status().is2xxSuccessful());
//        MvcResult mvcResult = mockMvc
//            .perform(
//                get("/buy/pay")
//                    .header("Authorization", token))
//            .andReturn();
//
//        validateReceipt(mvcResult);
//    }
//
//    private void validateReceipt(MvcResult mvcResult) throws IOException {
//        String json = mvcResult.getResponse().getContentAsString();
//        final Receipt receipt = mapper.readValue(json, Receipt.class);
//
//        assertNotNull(receipt);
//        receipt.getContent().forEach(product -> {
//            assertNotNull(product.getPriceWithoutPromotion());
//            assertNotNull(product.getUnits());
//            assertNotNull(product.getName());
//        });
//    }
//
//    private void generateToken() throws Exception {
//        MockMvc tokenMVC = MockMvcBuilders
//            .standaloneSetup(authenticationController)
//            .build();
//        MvcResult mvcResult = tokenMVC
//            .perform(post("/login")
//                .param("name", "ADMIN"))
//            .andReturn();
//        String json = mvcResult.getResponse().getContentAsString();
//        token = mapper.readValue(json, ObjectNode.class).get("value").asText();
//    }
//
//}