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


    @Autowired
    public AuthFilter(IAuthenticationDomain authenticationDomain) {
        super();
        this.authenticationDomain = authenticationDomain;
    }


    //// TODO: 04.12.17 cos z tym boolean
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = ((HttpServletRequest) servletRequest);
        HttpServletResponse response = ((HttpServletResponse) servletResponse);

        String authHeader = request.getHeader("Authorization");
        String requestUrl = request.getRequestURI();
//        boolean api = requestUrl.startsWith("/");
        boolean api = true;
        boolean authenticated = authenticationDomain.containsToken(new Token(authHeader));

        if (!api && !authenticated) {
            response.setStatus(401);
            response.getWriter().write("Unauthorized");
            response.getWriter().flush();
            response.getWriter().close();
        }

        filterChain.doFilter(request, response);

    }
}
