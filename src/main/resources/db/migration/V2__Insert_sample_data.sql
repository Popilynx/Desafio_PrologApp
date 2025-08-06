-- Inserção de dados de exemplo para o sistema

-- Inserir usuários de exemplo
INSERT INTO usuarios (email, nome, senha, role, created_at, updated_at) VALUES
('admin@empresa.com', 'Administrador', '$2a$10$iUZnxmcWFtpAni9euseIpe/i1XICgSP/wJ.nslG4kfXh6yUbGVsFG', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('joao@empresa.com', 'João Silva', '$2a$10$iUZnxmcWFtpAni9euseIpe/i1XICgSP/wJ.nslG4kfXh6yUbGVsFG', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('maria@empresa.com', 'Maria Santos', '$2a$10$iUZnxmcWFtpAni9euseIpe/i1XICgSP/wJ.nslG4kfXh6yUbGVsFG', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('pedro@empresa.com', 'Pedro Costa', '$2a$10$iUZnxmcWFtpAni9euseIpe/i1XICgSP/wJ.nslG4kfXh6yUbGVsFG', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- Inserir veículos de exemplo
INSERT INTO vehicles (placa, marca, quilometragem, status, created_at, updated_at) VALUES
('ABC1234', 'Ford', 50000, 'ATIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DEF5678', 'Chevrolet', 75000, 'ATIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('GHI9012', 'Volkswagen', 30000, 'ATIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('JKL3456', 'Toyota', 120000, 'INATIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('MNO7890', 'Honda', 45000, 'ATIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PQR1234', 'Hyundai', 60000, 'ATIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('STU5678', 'Fiat', 25000, 'ATIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('VWX9012', 'Renault', 80000, 'ATIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (placa) DO NOTHING;

-- Inserir pneus de exemplo
INSERT INTO tires (numero_fogo, marca, pressao_atual, status, created_at, updated_at) VALUES
('P001-2024-001', 'Michelin', 32.5, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P001-2024-002', 'Michelin', 32.0, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P001-2024-003', 'Michelin', 31.8, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P001-2024-004', 'Michelin', 32.2, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P002-2024-001', 'Bridgestone', 33.0, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P002-2024-002', 'Bridgestone', 32.8, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P002-2024-003', 'Bridgestone', 32.5, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P002-2024-004', 'Bridgestone', 32.7, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P003-2024-001', 'Goodyear', 31.5, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P003-2024-002', 'Goodyear', 31.8, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P003-2024-003', 'Goodyear', 32.0, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P003-2024-004', 'Goodyear', 31.7, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P004-2024-001', 'Pirelli', 33.2, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P004-2024-002', 'Pirelli', 33.0, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P004-2024-003', 'Pirelli', 32.8, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P004-2024-004', 'Pirelli', 33.1, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P005-2024-001', 'Continental', 32.3, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P005-2024-002', 'Continental', 32.1, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P005-2024-003', 'Continental', 32.4, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('P005-2024-004', 'Continental', 32.0, 'DISPONIVEL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (numero_fogo) DO NOTHING;

-- Associar pneus aos veículos (vehicle_tires)
-- Veículo 1 (ABC1234) - 4 pneus Michelin
INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'DIANTEIRA_ESQUERDA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'ABC1234' AND t.numero_fogo = 'P001-2024-001'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'DIANTEIRA_ESQUERDA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'DIANTEIRA_DIREITA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'ABC1234' AND t.numero_fogo = 'P001-2024-002'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'DIANTEIRA_DIREITA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'TRASEIRA_ESQUERDA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'ABC1234' AND t.numero_fogo = 'P001-2024-003'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'TRASEIRA_ESQUERDA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'TRASEIRA_DIREITA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'ABC1234' AND t.numero_fogo = 'P001-2024-004'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'TRASEIRA_DIREITA');

-- Veículo 2 (DEF5678) - 4 pneus Bridgestone
INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'DIANTEIRA_ESQUERDA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'DEF5678' AND t.numero_fogo = 'P002-2024-001'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'DIANTEIRA_ESQUERDA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'DIANTEIRA_DIREITA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'DEF5678' AND t.numero_fogo = 'P002-2024-002'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'DIANTEIRA_DIREITA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'TRASEIRA_ESQUERDA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'DEF5678' AND t.numero_fogo = 'P002-2024-003'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'TRASEIRA_ESQUERDA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'TRASEIRA_DIREITA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'DEF5678' AND t.numero_fogo = 'P002-2024-004'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'TRASEIRA_DIREITA');

-- Veículo 3 (GHI9012) - 4 pneus Goodyear
INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'DIANTEIRA_ESQUERDA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'GHI9012' AND t.numero_fogo = 'P003-2024-001'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'DIANTEIRA_ESQUERDA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'DIANTEIRA_DIREITA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'GHI9012' AND t.numero_fogo = 'P003-2024-002'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'DIANTEIRA_DIREITA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'TRASEIRA_ESQUERDA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'GHI9012' AND t.numero_fogo = 'P003-2024-003'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'TRASEIRA_ESQUERDA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'TRASEIRA_DIREITA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'GHI9012' AND t.numero_fogo = 'P003-2024-004'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'TRASEIRA_DIREITA');

-- Veículo 4 (MNO7890) - 4 pneus Pirelli
INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'DIANTEIRA_ESQUERDA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'MNO7890' AND t.numero_fogo = 'P004-2024-001'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'DIANTEIRA_ESQUERDA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'DIANTEIRA_DIREITA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'MNO7890' AND t.numero_fogo = 'P004-2024-002'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'DIANTEIRA_DIREITA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'TRASEIRA_ESQUERDA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'MNO7890' AND t.numero_fogo = 'P004-2024-003'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'TRASEIRA_ESQUERDA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'TRASEIRA_DIREITA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'MNO7890' AND t.numero_fogo = 'P004-2024-004'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'TRASEIRA_DIREITA');

-- Veículo 5 (PQR1234) - 4 pneus Continental
INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'DIANTEIRA_ESQUERDA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'PQR1234' AND t.numero_fogo = 'P005-2024-001'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'DIANTEIRA_ESQUERDA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'DIANTEIRA_DIREITA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'PQR1234' AND t.numero_fogo = 'P005-2024-002'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'DIANTEIRA_DIREITA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'TRASEIRA_ESQUERDA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'PQR1234' AND t.numero_fogo = 'P005-2024-003'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'TRASEIRA_ESQUERDA');

INSERT INTO vehicle_tires (vehicle_id, tire_id, position, created_at, updated_at)
SELECT v.id, t.id, 'TRASEIRA_DIREITA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM vehicles v, tires t
WHERE v.placa = 'PQR1234' AND t.numero_fogo = 'P005-2024-004'
AND NOT EXISTS (SELECT 1 FROM vehicle_tires vt WHERE vt.vehicle_id = v.id AND vt.position = 'TRASEIRA_DIREITA'); 