package com.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respostas de autenticação (login/registro).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    
    private String token;
    private String tipo = "Bearer";
    private String email;
    private String nome;
    private String role;
    
    public AuthResponseDTO(String token, String email, String nome, String role) {
        this.token = token;
        this.email = email;
        this.nome = nome;
        this.role = role;
    }
} 