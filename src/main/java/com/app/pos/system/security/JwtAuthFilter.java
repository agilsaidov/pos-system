package com.app.pos.system.security;


import com.app.pos.system.exception.JwtFilterException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final JwtUtils jwtUtils;
    private final HandlerExceptionResolver resolver;

    public JwtAuthFilter(JwtDecoder jwtDecoder,
                         JwtUtils jwtUtils,
                         @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {

        this.jwtDecoder = jwtDecoder;
        this.jwtUtils = jwtUtils;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try{
            Jwt jwt = jwtDecoder.decode(token);

            var authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            jwt, null, jwtUtils.extractAuthorities(jwt));

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }catch (JwtException e){
            SecurityContextHolder.clearContext();
            resolver.resolveException(request, response, null, e);
            return;

        } catch (Exception e){
            SecurityContextHolder.clearContext();
            resolver.resolveException(
                    request,
                    response,
                    null,
                    new JwtFilterException("Failed to authenticate token")
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}