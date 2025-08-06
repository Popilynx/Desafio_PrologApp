package com.desafio.controller;

import com.desafio.dto.PneuDTO;
import com.desafio.service.PneuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para gerenciar pneus
 */
@Slf4j
@RestController
@RequestMapping("/api/pneus")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PneuController {
    
    private final PneuService pneuService;
    
    /**
     * Cria um novo pneu
     */
    @PostMapping
    public ResponseEntity<PneuDTO> criarPneu(@Valid @RequestBody PneuDTO pneuDTO) {
        log.info("Criando pneu com número de fogo: {}", pneuDTO.getNumeroFogo());
        
        PneuDTO novoPneu = pneuService.criarPneu(pneuDTO);
        
        log.info("Pneu criado! ID: {}", novoPneu.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPneu);
    }
} 