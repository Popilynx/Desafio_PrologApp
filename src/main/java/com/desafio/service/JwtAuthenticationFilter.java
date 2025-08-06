package com.desafio.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT para interceptar requisições e validar tokens de autenticação.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        
        // Verificar se o header Authorization existe e começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Extrair o token JWT (remover "Bearer " do início)
        jwt = authHeader.substring(7);
        
        try {
            // Extrair o email do token
            userEmail = jwtService.extractUsername(jwt);
            
            // Se temos um email e não há usuário autenticado no contexto
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Buscar detalhes do usuário
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                // Validar o token
                if (jwtService.validateToken(jwt, userDetails)) {
                    
                    // Criar token de autenticação
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    
                    // Definir detalhes da autenticação
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Definir autenticação no contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("Usuário autenticado via JWT: {}", userEmail);
                }
            }
        } catch (Exception e) {
            log.warn("Erro ao processar token JWT: {}", e.getMessage());
        }
        
        // Continuar com o filtro
        filterChain.doFilter(request, response);
    }
} 