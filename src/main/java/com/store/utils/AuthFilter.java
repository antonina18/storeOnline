package com.store.utils;

import com.store.domain.IAuthenticationDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends GenericFilterBean {

    private static final String APP_URL = "/storeOnline";

    private IAuthenticationDomain authenticationDomain;

    private static final List<String> ALLOWED_PATHS = Arrays.asList("", APP_URL + "/login", "/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**");

    @Autowired
    public AuthFilter(IAuthenticationDomain authenticationDomain) {
        super();
        this.authenticationDomain = authenticationDomain;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = ((HttpServletRequest) servletRequest);
        HttpServletResponse response = ((HttpServletResponse) servletResponse);

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String requestUrl = request.getRequestURI();
        boolean correctUrl = requestUrl.startsWith(APP_URL);
        boolean authenticated = authenticationDomain.containsToken(new Token(authHeader));
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");


        boolean allowedPath = ALLOWED_PATHS.contains(path);

        if (allowedPath || authenticated) {
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }


    }

    private void checkAuthorization(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String requestUrl = request.getRequestURI();
        boolean correctUrl = requestUrl.startsWith(APP_URL);
        boolean authenticated = authenticationDomain.containsToken(new Token(authHeader));
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");


        boolean allowedPath = ALLOWED_PATHS.contains(path);

        if (allowedPath || authenticated) {
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }

    }

//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = ((HttpServletRequest) servletRequest);
//        HttpServletResponse response = ((HttpServletResponse) servletResponse);
//
//        String authHeader = request.getHeader("Authorization");
//        String requestUrl = request.getRequestURI();
//        boolean authenticated = authenticationDomain.containsToken(new Token(authHeader));
//
//                String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
//        boolean allowedPath = ALLOWED_PATHS.contains(path);
////
//        if (!allowedPath) {
//            response.setStatus(401);
//            response.getWriter().write("Unauthorized");
//            response.getWriter().flush();
//            response.getWriter().close();
//        }
//
//        filterChain.doFilter(request, response);
//
//    }
}
