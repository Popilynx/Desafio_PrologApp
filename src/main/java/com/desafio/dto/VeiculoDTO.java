package com.desafio.dto;

import com.desafio.model.StatusVeiculo;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public class VeiculoDTO {
    
    private Long id;
    
    @NotBlank(message = "Placa é obrigatória")
    @Pattern(regexp = "[A-Z]{3}[0-9][0-9A-Z][0-9]{2}", message = "Placa deve estar no formato Mercosul (ABC1D23)")
    private String placa;
    
    @NotBlank(message = "Marca é obrigatória")
    private String marca;
    
    @NotNull(message = "Quilometragem é obrigatória")
    @Positive(message = "Quilometragem deve ser positiva")
    @JsonProperty("quilometragem")
    private Integer quilometragem;
    
    private StatusVeiculo status = StatusVeiculo.ATIVO;
    
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    // Lista de pneus aplicados (para resposta)
    private List<VeiculoPneuDTO> pneusAplicados;
    
    // Construtor padrão
    public VeiculoDTO() {}
    
    // Construtor com parâmetros básicos
    public VeiculoDTO(String placa, String marca, Integer quilometragem) {
        this.placa = placa;
        this.marca = marca;
        this.quilometragem = quilometragem;
    }
    
    // Construtor completo (para respostas)
    public VeiculoDTO(Long id, String placa, String marca, Integer quilometragem, 
                      StatusVeiculo status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.quilometragem = quilometragem;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
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
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public List<VeiculoPneuDTO> getPneusAplicados() { return pneusAplicados; }
    public void setPneusAplicados(List<VeiculoPneuDTO> pneusAplicados) { this.pneusAplicados = pneusAplicados; }
} 