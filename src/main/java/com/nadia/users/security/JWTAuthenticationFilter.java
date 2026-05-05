package com.nadia.users.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nadia.users.entities.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super();
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        org.springframework.security.core.userdetails.User springUser =
            (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        List<String> roles = new ArrayList<>();
        springUser.getAuthorities().forEach(au -> roles.add(au.getAuthority()));

        String jwt = JWT.create()
            .withSubject(springUser.getUsername())
            .withArrayClaim("roles", roles.toArray(new String[0]))
            .withExpiresAt(new Date(System.currentTimeMillis() + SecParams.EXP_TIME))
            .sign(Algorithm.HMAC256(SecParams.SECRET));

        response.addHeader("Authorization", SecParams.PREFIX + jwt);
    }

    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        if (failed instanceof DisabledException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403
            response.setContentType("application/json");

            Map<String, Object> data = new HashMap<>();
            data.put("errorCause", "disabled");
            data.put("message", "Compte désactivé. Veuillez valider votre email.");

            PrintWriter writer = response.getWriter();
            writer.println(new ObjectMapper().writeValueAsString(data));
            writer.flush();
        } else {
            super.unsuccessfulAuthentication(request, response, failed);
        }
    }
}