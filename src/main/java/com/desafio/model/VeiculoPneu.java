package com.desafio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_tires", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"vehicle_id", "position"})
})
public class VeiculoPneu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Veiculo veiculo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tire_id", nullable = false)
    private Pneu pneu;
    
    @NotBlank(message = "Posição é obrigatória")
    @Column(nullable = false)
    private String position;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Construtor padrão
    public VeiculoPneu() {}
    
    // Construtor com parâmetros
    public VeiculoPneu(Veiculo veiculo, Pneu pneu, String position) {
        this.veiculo = veiculo;
        this.pneu = pneu;
        this.position = position;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }
    
    public Pneu getPneu() { return pneu; }
    public void setPneu(Pneu pneu) { this.pneu = pneu; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 