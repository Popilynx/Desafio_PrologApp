-- Criação das tabelas para o sistema de veículos e pneus

-- Tabela de veículos
CREATE TABLE vehicles (
    id BIGSERIAL PRIMARY KEY,
    placa VARCHAR(10) NOT NULL UNIQUE,
    marca VARCHAR(100) NOT NULL,
    quilometragem INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de pneus
CREATE TABLE tires (
    id BIGSERIAL PRIMARY KEY,
    numero_fogo VARCHAR(50) NOT NULL UNIQUE,
    marca VARCHAR(100) NOT NULL,
    pressao_atual DOUBLE PRECISION NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de relacionamento entre veículos e pneus
CREATE TABLE vehicle_tires (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    tire_id BIGINT NOT NULL,
    position VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE,
    FOREIGN KEY (tire_id) REFERENCES tires(id) ON DELETE CASCADE,
    UNIQUE(vehicle_id, position)
);

-- Tabela de usuários
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para melhorar performance
CREATE INDEX idx_vehicles_placa ON vehicles(placa);
CREATE INDEX idx_vehicles_status ON vehicles(status);
CREATE INDEX idx_tires_numero_fogo ON tires(numero_fogo);
CREATE INDEX idx_tires_status ON tires(status);
CREATE INDEX idx_vehicle_tires_vehicle_id ON vehicle_tires(vehicle_id);
CREATE INDEX idx_vehicle_tires_tire_id ON vehicle_tires(tire_id);
CREATE INDEX idx_vehicle_tires_position ON vehicle_tires(position);
CREATE INDEX idx_usuarios_email ON usuarios(email); 