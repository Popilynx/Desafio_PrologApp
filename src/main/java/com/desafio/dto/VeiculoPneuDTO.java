package com.desafio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class VeiculoPneuDTO {
    
    private Long id;
    
    @NotNull(message = "ID do veículo é obrigatório")
    private Long idVeiculo;
    
    @NotNull(message = "ID do pneu é obrigatório")
    private Long idPneu;
    
    @NotBlank(message = "Posição é obrigatória")
    private String posicao;
    
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    // Dados do pneu (para facilitar a resposta)
    private String numeroFogo;
    private String marcaPneu;
    private Double pressaoAtual;
    
    // Construtor padrão
    public VeiculoPneuDTO() {}
    
    // Construtor com parâmetros básicos
    public VeiculoPneuDTO(Long idVeiculo, Long idPneu, String posicao) {
        this.idVeiculo = idVeiculo;
        this.idPneu = idPneu;
        this.posicao = posicao;
    }
    
    // Construtor completo (para respostas)
    public VeiculoPneuDTO(Long id, Long idVeiculo, Long idPneu, String posicao, 
                          LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.idVeiculo = idVeiculo;
        this.idPneu = idPneu;
        this.posicao = posicao;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }
    
    // Construtor com dados do pneu (para respostas completas)
    public VeiculoPneuDTO(Long id, Long idVeiculo, Long idPneu, String posicao, 
                          String numeroFogo, String marcaPneu, Double pressaoAtual,
                          LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.idVeiculo = idVeiculo;
        this.idPneu = idPneu;
        this.posicao = posicao;
        this.numeroFogo = numeroFogo;
        this.marcaPneu = marcaPneu;
        this.pressaoAtual = pressaoAtual;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getIdVeiculo() { return idVeiculo; }
    public void setIdVeiculo(Long idVeiculo) { this.idVeiculo = idVeiculo; }
    
    public Long getIdPneu() { return idPneu; }
    public void setIdPneu(Long idPneu) { this.idPneu = idPneu; }
    
    public String getPosicao() { return posicao; }
    public void setPosicao(String posicao) { this.posicao = posicao; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public String getNumeroFogo() { return numeroFogo; }
    public void setNumeroFogo(String numeroFogo) { this.numeroFogo = numeroFogo; }
    
    public String getMarcaPneu() { return marcaPneu; }
    public void setMarcaPneu(String marcaPneu) { this.marcaPneu = marcaPneu; }
    
    public Double getPressaoAtual() { return pressaoAtual; }
    public void setPressaoAtual(Double pressaoAtual) { this.pressaoAtual = pressaoAtual; }
} 