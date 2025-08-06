package com.desafio.repository;

import com.desafio.model.Pneu;
import com.desafio.model.StatusPneu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PneuRepository extends JpaRepository<Pneu, Long> {
    
    // Buscar pneu por número de fogo
    Optional<Pneu> findByNumeroFogo(String numeroFogo);
    
    // Verificar se existe pneu com este número de fogo
    boolean existsByNumeroFogo(String numeroFogo);
    
    // Buscar pneus por marca
    List<Pneu> findByMarcaContainingIgnoreCase(String marca);
    
    // Buscar pneus por status
    List<Pneu> findByStatus(StatusPneu status);
    
    // Buscar pneus disponíveis
    List<Pneu> findByStatusOrderByNumeroFogo(StatusPneu status);
    
    // Buscar pneus com pressão menor que X
    List<Pneu> findByPressaoAtualLessThan(Double pressaoAtual);
    
    // Buscar pneus com pressão entre X e Y
    List<Pneu> findByPressaoAtualBetween(Double pressaoMinima, Double pressaoMaxima);
} 