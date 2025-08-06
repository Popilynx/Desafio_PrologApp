package com.desafio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TesteSimples {

    @Test
    void contextLoads() {
        // Teste básico para verificar se o contexto Spring carrega
        System.out.println("Contexto Spring carregado com sucesso!");
    }
} 