package com.store.utils;

import lombok.Data;

@Data
public class Response<T> {

    public static final String OK = "OK";
    private static final String EMPTY = "";
    private String message;
    private T data;

    private Response() {
        this.message = EMPTY;
    }

    public static <T> Response<T> create(T data, String message) {
        Response response = new Response();
        response.data = data;
        if (message != null) {
            response.message = message;
        }
        return response;
    }

    public static Response create(String message) {
        Response response = new Response();
        if (message != null) {
            response.message = message;
        }
        return response;
    }

    public static Response success() {
        Response response = new Response();
        response.message = OK;
        return response;
    }


}
