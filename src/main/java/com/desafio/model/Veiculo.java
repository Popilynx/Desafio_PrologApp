package com.desafio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Veiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Placa é obrigatória")
    @Pattern(regexp = "[A-Z]{3}[0-9][0-9A-Z][0-9]{2}", message = "Placa deve estar no formato Mercosul (ABC1D23)")
    @Column(unique = true, nullable = false)
    private String placa;
    
    @NotBlank(message = "Marca é obrigatória")
    @Column(nullable = false)
    private String marca;
    
    @NotNull(message = "Quilometragem é obrigatória")
    @Positive(message = "Quilometragem deve ser positiva")
    @Column(name = "quilometragem", nullable = false)
    private Integer quilometragem;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusVeiculo status = StatusVeiculo.ATIVO;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VeiculoPneu> veiculoPneus;
    
    // Construtor padrão
    public Veiculo() {}
    
    // Construtor com parâmetros
    public Veiculo(String placa, String marca, Integer quilometragem) {
        this.placa = placa;
        this.marca = marca;
        this.quilometragem = quilometragem;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    
    public Integer getQuilometragem() { return quilometragem; }
    public void setQuilometragem(Integer quilometragem) { this.quilometragem = quilometragem; }
    
    public StatusVeiculo getStatus() { return status; }
    public void setStatus(StatusVeiculo status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<VeiculoPneu> getVeiculoPneus() { return veiculoPneus; }
    public void setVeiculoPneus(List<VeiculoPneu> veiculoPneus) { this.veiculoPneus = veiculoPneus; }
} 