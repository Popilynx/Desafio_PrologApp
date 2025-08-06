package com.desafio.repository;

import com.desafio.model.VeiculoPneu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeiculoPneuRepository extends JpaRepository<VeiculoPneu, Long> {
    
    // Buscar todos os pneus de um veículo
    List<VeiculoPneu> findByVeiculoId(Long veiculoId);
    
    // Buscar pneu em uma posição específica de um veículo
    Optional<VeiculoPneu> findByVeiculoIdAndPosition(Long veiculoId, String position);
    
    // Verificar se uma posição está ocupada em um veículo
    boolean existsByVeiculoIdAndPosition(Long veiculoId, String position);
    
    // Buscar todos os veículos onde um pneu está aplicado
    List<VeiculoPneu> findByPneuId(Long pneuId);
    
    // Buscar aplicação específica de um pneu
    Optional<VeiculoPneu> findByVeiculoIdAndPneuId(Long veiculoId, Long pneuId);
    
    // Verificar se um pneu está aplicado em algum veículo
    boolean existsByPneuId(Long pneuId);
    
    // Buscar aplicações por posição (útil para estatísticas)
    List<VeiculoPneu> findByPosition(String position);
} 