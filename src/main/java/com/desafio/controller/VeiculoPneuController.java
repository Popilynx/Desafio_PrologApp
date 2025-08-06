package com.desafio.controller;

import com.desafio.dto.VeiculoPneuDTO;
import com.desafio.service.VeiculoPneuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/veiculos-pneus")
@CrossOrigin(origins = "*")
public class VeiculoPneuController {
    
    @Autowired
    private VeiculoPneuService veiculoPneuService;
    
    /**
     * 5. Endpoint para vincular um pneu em um veículo
     * Este endpoint deve vincular um pneu com um veículo.
     * Lembre-se: um veículo não pode ter dois pneus na mesma posição.
     * 
     * @param veiculoPneuDTO Dados da vinculação
     * @return Vinculação criada
     */
    @PostMapping
    public ResponseEntity<VeiculoPneuDTO> vincularPneuAoVeiculo(@Valid @RequestBody VeiculoPneuDTO veiculoPneuDTO) {
        VeiculoPneuDTO aplicacao = veiculoPneuService.vincularPneuAoVeiculo(veiculoPneuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(aplicacao);
    }
    
    /**
     * 6. Endpoint para desvincular um pneu em um veículo
     * Este endpoint deve desvincular um pneu com um veículo.
     * 
     * @param idVeiculo ID do veículo
     * @param idPneu ID do pneu
     * @return Sem conteúdo
     */
    @DeleteMapping("/{idVeiculo}/pneu/{idPneu}")
    public ResponseEntity<Void> desvincularPneuDoVeiculo(@PathVariable Long idVeiculo, @PathVariable Long idPneu) {
        veiculoPneuService.desvincularPneuDoVeiculo(idVeiculo, idPneu);
        return ResponseEntity.noContent().build();
    }
} 