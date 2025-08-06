package com.desafio.dto;

import com.desafio.model.StatusPneu;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class PneuDTO {
    
    private Long id;
    
    @NotBlank(message = "Número de fogo é obrigatório")
    private String numeroFogo;
    
    @NotBlank(message = "Marca é obrigatória")
    private String marca;
    
    @NotNull(message = "Pressão atual é obrigatória")
    @Positive(message = "Pressão deve ser positiva")
    private Double pressaoAtual;
    
    private StatusPneu status = StatusPneu.DISPONIVEL;
    
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    // Construtor padrão
    public PneuDTO() {}
    
    // Construtor com parâmetros básicos
    public PneuDTO(String numeroFogo, String marca, Double pressaoAtual) {
        this.numeroFogo = numeroFogo;
        this.marca = marca;
        this.pressaoAtual = pressaoAtual;
    }
    
    // Construtor completo (para respostas)
    public PneuDTO(Long id, String numeroFogo, String marca, Double pressaoAtual, 
                   StatusPneu status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.numeroFogo = numeroFogo;
        this.marca = marca;
        this.pressaoAtual = pressaoAtual;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
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
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
} 