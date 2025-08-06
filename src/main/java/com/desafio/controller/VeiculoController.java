package com.desafio.controller;

import com.desafio.dto.VeiculoDTO;
import com.desafio.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerenciar veículos
 */
@Slf4j
@RestController
@RequestMapping("/api/veiculos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VeiculoController {
    
    private final VeiculoService veiculoService;
    
    /**
     * Lista todos os veículos (sem pneus)
     */
    @GetMapping
    public ResponseEntity<List<VeiculoDTO>> buscarTodosVeiculos() {
        log.info("Listando todos os veículos");
        
        List<VeiculoDTO> veiculos = veiculoService.buscarTodosVeiculos();
        return ResponseEntity.ok(veiculos);
    }
    
    /**
     * Busca um veículo específico com seus pneus
     */
    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> buscarVeiculoComPneusPorId(@PathVariable Long id) {
        log.info("Buscando veículo com pneus ID: {}", id);
        
        VeiculoDTO veiculo = veiculoService.buscarVeiculoComPneusPorId(id);
        return ResponseEntity.ok(veiculo);
    }
    
    /**
     * Cria um novo veículo
     */
    @PostMapping
    public ResponseEntity<VeiculoDTO> criarVeiculo(@Valid @RequestBody VeiculoDTO veiculoDTO) {
        log.info("Criando veículo com placa: {}", veiculoDTO.getPlaca());
        
        VeiculoDTO novoVeiculo = veiculoService.criarVeiculo(veiculoDTO);
        
        log.info("Veículo criado! ID: {}", novoVeiculo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(novoVeiculo);
    }
} 