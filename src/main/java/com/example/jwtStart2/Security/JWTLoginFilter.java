package com.example.jwtStart2.Security;

import org.springframework.security.authentication.AuthenticationManager;

import javax.servlet.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
//toDo: to create class for filter and generate token in first attempt login
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        //toDo: 7- set credential
        setAuthenticationManager(authManager);
    }

    // user attempt to login using username and password
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, IOException, ServletException {
        // get from request username and password
        AccountCredentials creds = new ObjectMapper()
                .readValue(req.getInputStream(), AccountCredentials.class);

        // authenticate user
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getUsername(),
                        creds.getPassword(),
                        Collections.emptyList()
                )
        );
    }

    // in case the user was authentication do ...
    // note you may implement or override	*unsuccessfulAuthentication* method for unsuccessful authentication
    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {
        TokenAuthenticationService
                .addAuthentication(res, auth.getName());
    }
}