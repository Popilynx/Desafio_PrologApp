package com.desafio.constant;

/**
 * Constantes utilizadas na API.
 * Centraliza valores importantes como URLs, mensagens e configurações.
 */
public final class ApiConstants {
    
    // URLs base
    public static final String API_BASE_PATH = "/api";
    public static final String VEHICLES_PATH = "/veiculos";
    public static final String TIRES_PATH = "/pneus";
    public static final String VEHICLE_TIRES_PATH = "/veiculos-pneus";
    public static final String TEST_PATH = "/test";
    
    // Mensagens de erro
    public static final String VEHICLE_NOT_FOUND = "Veículo não encontrado";
    public static final String TIRE_NOT_FOUND = "Pneu não encontrado";
    public static final String PLATE_ALREADY_EXISTS = "Placa já cadastrada";
    public static final String FIRE_NUMBER_ALREADY_EXISTS = "Número de fogo já cadastrado";
    public static final String VEHICLE_HAS_TIRES = "Não é possível deletar veículo com pneus aplicados";
    public static final String TIRE_IS_APPLIED = "Não é possível deletar pneu que está aplicado em um veículo";
    
    // Valores padrão
    public static final double DEFAULT_TIRE_PRESSURE = 32.0;
    public static final double LOW_PRESSURE_THRESHOLD = 30.0;
    public static final String API_VERSION = "1.0.0";
    
    // Headers
    public static final String CONTENT_TYPE = "application/json";
    public static final String ACCEPT = "application/json";
    
    // Construtor privado para evitar instanciação
    private ApiConstants() {
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada");
    }
} 