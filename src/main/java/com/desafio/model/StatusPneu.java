package com.desafio.model;

public enum StatusPneu {
    EM_USO("Em Uso"),
    DISPONIVEL("Disponível");
    
    private final String description;
    
    StatusPneu(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 