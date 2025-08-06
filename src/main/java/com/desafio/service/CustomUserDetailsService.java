package com.desafio.service;

import com.desafio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço personalizado para carregar detalhes do usuário.
 * Implementa UserDetailsService para integração com Spring Security.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Carregando usuário por email: {}", email);
        
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("Usuário não encontrado com email: {}", email);
                return new UsernameNotFoundException("Usuário não encontrado com email: " + email);
            });
    }
} 