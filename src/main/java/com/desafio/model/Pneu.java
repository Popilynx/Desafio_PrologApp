package com.desafio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tires")
public class Pneu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Número de fogo é obrigatório")
    @Column(name = "numero_fogo", unique = true, nullable = false)
    private String numeroFogo;
    
    @NotBlank(message = "Marca é obrigatória")
    @Column(nullable = false)
    private String marca;
    
    @NotNull(message = "Pressão atual é obrigatória")
    @Positive(message = "Pressão deve ser positiva")
    @Column(name = "pressao_atual", nullable = false)
    private Double pressaoAtual;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPneu status = StatusPneu.DISPONIVEL;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "pneu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VeiculoPneu> veiculoPneus;
    
    // Construtor padrão
    public Pneu() {}
    
    // Construtor com parâmetros
    public Pneu(String numeroFogo, String marca, Double pressaoAtual) {
        this.numeroFogo = numeroFogo;
        this.marca = marca;
        this.pressaoAtual = pressaoAtual;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumeroFogo() { return numeroFogo; }
    public void setNumeroFogo(String numeroFogo) { this.numeroFogo = numeroFogo; }
    
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    
    public Double getPressaoAtual() { return pressaoAtual; }
    public void setPressaoAtual(Double pressaoAtual) { this.pressaoAtual = pressaoAtual; }
    
    public StatusPneu getStatus() { return status; }
    public void setStatus(StatusPneu status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<VeiculoPneu> getVeiculoPneus() { return veiculoPneus; }
    public void setVeiculoPneus(List<VeiculoPneu> veiculoPneus) { this.veiculoPneus = veiculoPneus; }
} 