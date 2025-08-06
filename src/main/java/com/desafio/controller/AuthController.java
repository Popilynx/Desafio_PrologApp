package com.desafio.controller;

import com.desafio.dto.AuthResponseDTO;
import com.desafio.dto.LoginRequestDTO;
import com.desafio.dto.RegistroRequestDTO;
import com.desafio.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para autenticação e registro de usuários.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Registra um novo usuário no sistema.
     * 
     * @param request Dados do usuário para registro
     * @return Resposta com token JWT
     */
    @PostMapping(value = "/registrar", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO request) {
        try {
            log.info("=== INICIO REGISTRO ===");
            log.info("Recebendo requisição para registrar usuário: {}", request.getEmail());
            
            AuthResponseDTO response = authService.registrar(request);
            
            log.info("Usuário registrado com sucesso: {}", request.getEmail());
            log.info("=== FIM REGISTRO ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("=== ERRO NO CONTROLLER ===", e);
            throw e;
        }
    }
    
    /**
     * Autentica um usuário existente.
     * 
     * @param request Dados de login
     * @return Resposta com token JWT
     */
    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Recebendo requisição de login: {}", request.getEmail());
        
        AuthResponseDTO response = authService.login(request);
        
        log.info("Login realizado com sucesso: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint de teste para verificar se a autenticação está funcionando.
     * 
     * @return Mensagem de sucesso
     */
    @GetMapping("/teste")
    public ResponseEntity<String> teste() {
        return ResponseEntity.ok("Autenticação funcionando!");
    }
} 