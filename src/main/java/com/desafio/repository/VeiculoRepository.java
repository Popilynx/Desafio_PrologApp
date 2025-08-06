package com.desafio.repository;

import com.desafio.model.Veiculo;
import com.desafio.model.StatusVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    
    // Buscar veículo por placa
    Optional<Veiculo> findByPlaca(String placa);
    
    // Verificar se existe veículo com esta placa
    boolean existsByPlaca(String placa);
    
    // Buscar veículos por marca
    List<Veiculo> findByMarcaContainingIgnoreCase(String marca);
    
    // Buscar veículos por status
    List<Veiculo> findByStatus(StatusVeiculo status);
    
    // Buscar veículos ativos
    List<Veiculo> findByStatusOrderByPlaca(StatusVeiculo status);
    
    // Buscar veículos com quilometragem maior que X
    List<Veiculo> findByQuilometragemGreaterThan(Integer quilometragem);
} 