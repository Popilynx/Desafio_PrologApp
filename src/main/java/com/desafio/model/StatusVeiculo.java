package com.desafio.model;

public enum StatusVeiculo {
    ATIVO("Ativo"),
    INATIVO("Inativo");
    
    private final String description;
    
    StatusVeiculo(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 