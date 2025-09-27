package com.esporte.myapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Extrai o Bearer token, valida com JwtDecoder (JWKS do Clerk)
 * e popula o SecurityContext.
 */

public class ClerkJwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    public ClerkJwtAuthenticationFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Sem Bearer -> segue a cadeia sem autenticar
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            Jwt jwt = jwtDecoder.decode(token);

            String subject = jwt.getSubject(); // id do usuário no Clerk (sub)
            String email = jwt.getClaimAsString("email");

            // Opcional: extraia roles/permissions do token (ajuste conforme seu template/claims)
            @SuppressWarnings("unchecked")
            List<String> roles = Optional.ofNullable((List<String>) jwt.getClaims().get("roles"))
                    .orElse(Collections.emptyList());

            var authorities = roles.stream()
                    .filter(Objects::nonNull)
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r.toUpperCase())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            // Se não houver roles no token, pode dar uma default:
            if (authorities.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            JwtAuthenticationToken authentication =
                    new JwtAuthenticationToken(jwt, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // (Opcional) você pode guardar o email em um ThreadLocal/Contexto próprio
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException ex) {
            // Token inválido -> zera contexto e segue; Security dirá 401/403 depois
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}
