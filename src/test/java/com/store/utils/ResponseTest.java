package com.store.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class ResponseTest {

    private static final String EMPTY = "";
    private static final String TEST_MESSAGE = "TEST_MESSAGE";

    @Test
    public void createWithMessage() throws Exception {

        //when
        Response actual = Response.create(TEST_MESSAGE);

        //then
        assertThat(actual.getMessage(), is(equalTo(TEST_MESSAGE)));
    }

    @Test
    public void createNullMessage() throws Exception {

        //given
        final String message = null;

        //when
        final Response actual = Response.create(message);

        //then
        assertThat(actual.getMessage(), is(equalTo(EMPTY)));
    }

    @Test
    public void createWithDataAndMessage() throws Exception {
        //given
        final Integer expected = 5;

        //when
        final Response<Integer> actual = Response.create(5, TEST_MESSAGE);

        //then
        assertThat(actual.getMessage(), is(equalTo(TEST_MESSAGE)));
        assertThat(actual.getData(), is(equalTo(expected)));
    }

    @Test
    public void createWithNullMessage() throws Exception {
        //given
        final Integer expected = 5;

        //when
        final Response<Integer> actual = Response.create(5, null);

        //then
        assertThat(actual.getMessage(), is(equalTo(EMPTY)));
        assertThat(actual.getData(), is(equalTo(expected)));
    }

    @Test
    public void createWithNullData() throws Exception {
        //when
        Response<Integer> actual = Response.create(null, TEST_MESSAGE);

        //then
        assertThat(actual.getMessage(), is(equalTo(TEST_MESSAGE)));
        assertNull(actual.getData());
    }


    @Test
    public void create() throws Exception {
        //when
        final Response actual = Response.success();

        //then
        assertThat(actual.getMessage(), is(equalTo(Response.OK)));
        assertNull(actual.getData());
    }
}