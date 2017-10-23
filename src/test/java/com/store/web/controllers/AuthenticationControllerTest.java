package com.store.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private AuthenticationController controller;


    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(wac).build();
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc
                .perform(post("/login")
                        .param("name", "ADMIN"))
            .andExpect(status().is2xxSuccessful());


    }

    @Test
    public void testLoginUserNotFound() throws Exception {
        mockMvc
            .perform(post("/login")
                .param("name", "NO_ACTIVE"))
            .andExpect(status().is4xxClientError());

    }


}