package com.desafio.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para testes da API.
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    

    
    /**
     * Endpoint de teste para verificar se a API está funcionando.
     * 
     * @return Mensagem de sucesso
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> test() {
        log.info("Recebendo requisição de teste da API");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "API de Veículos e Pneus está funcionando!");
        response.put("version", "1.0.0");
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint para testar se o problema é específico do JWT.
     * 
     * @return Informações sobre o teste
     */
    @GetMapping("/simple")
    public ResponseEntity<Map<String, Object>> testSimple() {
        log.info("Testando endpoint simples");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Endpoint simples funcionando");
        response.put("status", "success");
        
        return ResponseEntity.ok(response);
    }
} 