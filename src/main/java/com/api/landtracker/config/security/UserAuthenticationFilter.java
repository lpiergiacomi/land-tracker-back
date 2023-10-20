package com.api.landtracker.config.security;

import com.api.landtracker.model.dto.UserRegisterDTO;
import com.api.landtracker.model.entities.User;
import com.api.landtracker.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final UserRepository userRepository;

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
            final Authentication authResult) throws JsonProcessingException {
        final String token = jwtUtil.createToken(authResult);

        ObjectMapper objectMapper = new ObjectMapper();
        User loggedUser = this.userRepository.findByUsername(((UserDetails)authResult.getPrincipal()).getUsername()).get();
        JsonNode jsonNode = objectMapper.createObjectNode().put("access-token", token).put("user-id", loggedUser.getId());
        String jsonString = objectMapper.writeValueAsString(jsonNode);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonString);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
