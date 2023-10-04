package com.api.landtracker.config.security;

import com.api.landtracker.model.dto.UserRegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final DaoAuthenticationProvider daoAuthenticationProvider;

    public UserAuthenticationFilter(
            final JwtUtil jwtUtil, final DaoAuthenticationProvider daoAuthenticationProvider) {
        this.jwtUtil = jwtUtil;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @Override
    public Authentication attemptAuthentication(
            final HttpServletRequest request, final HttpServletResponse response)
            throws AuthenticationException {

        final UserRegisterDTO userLogin;

        try {
            final BufferedReader bufferedReader = request.getReader();
            final ObjectMapper objectMapper = new ObjectMapper();
            userLogin = objectMapper.readValue(bufferedReader, UserRegisterDTO.class);

        } catch (final Exception e) {
            throw new RuntimeException();
        }

        return daoAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLogin.getUsername(), userLogin.getPassword()));
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult) {
        final String token = jwtUtil.createToken(authResult);

        response.setHeader("auth-token", token);
    }
}
