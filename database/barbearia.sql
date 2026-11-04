-- =====================================================================
--  SISTEMA DE BARBEARIA - Script de criação do banco (MySQL 8+)
--  Rotina principal: AGENDAMENTO DE ATENDIMENTO
-- =====================================================================

-- ---------------------------------------------------------------------
-- PARTE 1 - CRIAÇÃO DO BANCO
-- ---------------------------------------------------------------------
DROP DATABASE IF EXISTS barbearia;
CREATE DATABASE barbearia CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE barbearia;

-- ---------------------------------------------------------------------
-- PARTE 2 - TABELAS SIMPLES (os 4 CRUDs)
-- ---------------------------------------------------------------------

-- CRUD 1 - CLIENTE
CREATE TABLE cliente (
    id_cliente  INT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL,
    telefone    VARCHAR(20)  NOT NULL,
    email       VARCHAR(100)
);

-- CRUD 2 - PROFISSIONAL
CREATE TABLE profissional (
    id_profissional INT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL,
    telefone        VARCHAR(20),
    especialidade   VARCHAR(60),
    ativo           BOOLEAN NOT NULL DEFAULT TRUE
);

-- CRUD 3 - SERVICO
CREATE TABLE servico (
    id_servico   INT AUTO_INCREMENT PRIMARY KEY,
    descricao    VARCHAR(80)  NOT NULL,
    preco        DECIMAL(8,2) NOT NULL CHECK (preco >= 0),
    duracao_min  INT          NOT NULL CHECK (duracao_min > 0)
);

-- CRUD 4 - FORMA DE PAGAMENTO
CREATE TABLE forma_pagamento (
    id_forma_pagamento INT AUTO_INCREMENT PRIMARY KEY,
    descricao          VARCHAR(40) NOT NULL UNIQUE
);

-- ---------------------------------------------------------------------
-- PARTE 3 - TABELA PRINCIPAL (CRUD principal: AGENDAMENTO)
-- ---------------------------------------------------------------------
CREATE TABLE agendamento (
    id_agendamento     INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente         INT  NOT NULL,
    id_profissional    INT  NOT NULL,
    id_servico         INT  NOT NULL,
    id_forma_pagamento INT  NULL,                -- preenchida só ao concluir
    data_agendamento   DATE NOT NULL,
    hora               TIME NOT NULL,
    valor              DECIMAL(8,2) NOT NULL,    -- preço "congelado" no dia
    status             ENUM('AGENDADO','CONCLUIDO','CANCELADO') NOT NULL DEFAULT 'AGENDADO',
    observacao         VARCHAR(255),

    CONSTRAINT fk_agend_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente) ON DELETE RESTRICT,

    CONSTRAINT fk_agend_profissional FOREIGN KEY (id_profissional)
        REFERENCES profissional(id_profissional) ON DELETE RESTRICT,

    CONSTRAINT fk_agend_servico FOREIGN KEY (id_servico)
        REFERENCES servico(id_servico) ON DELETE RESTRICT,

    CONSTRAINT fk_agend_pagamento FOREIGN KEY (id_forma_pagamento)
        REFERENCES forma_pagamento(id_forma_pagamento) ON DELETE RESTRICT
);

-- ---------------------------------------------------------------------
-- PARTE 4 - DADOS PARA DEMONSTRAÇÃO
-- ---------------------------------------------------------------------
INSERT INTO cliente (nome, telefone, email) VALUES
('João Pereira',   '(61) 99111-2233', 'joao@email.com'),
('Lucas Andrade',  '(61) 98222-3344', 'lucas@email.com'),
('Rafael Moreira', '(61) 99333-4455', NULL),
('Bruno Carvalho', '(61) 98444-5566', 'bruno@email.com'),
('Felipe Santos',  '(61) 99555-6677', 'felipe@email.com');

INSERT INTO profissional (nome, telefone, especialidade) VALUES
('Carlos Souza',   '(61) 99000-1111', 'Corte e barba'),
('Marcos Lima',    '(61) 99000-2222', 'Degradê e desenhos'),
('Diego Ferreira', '(61) 99000-3333', 'Coloração e química');

INSERT INTO servico (descricao, preco, duracao_min) VALUES
('Corte masculino', 40.00, 30),
('Barba',           30.00, 20),
('Corte + Barba',   60.00, 50),
('Pigmentação',     50.00, 40),
('Sobrancelha',     15.00, 10);

INSERT INTO forma_pagamento (descricao) VALUES
('Dinheiro'), ('Pix'), ('Cartão de Débito'), ('Cartão de Crédito');

INSERT INTO agendamento
    (id_cliente, id_profissional, id_servico, id_forma_pagamento,
     data_agendamento, hora, valor, status, observacao)
VALUES
(1, 1, 3, 2,    '2026-11-02', '09:00', 60.00, 'CONCLUIDO', NULL),
(2, 2, 1, 1,    '2026-11-02', '10:00', 40.00, 'CONCLUIDO', 'Degradê baixo'),
(3, 1, 2, NULL, '2026-11-02', '11:00', 30.00, 'CANCELADO', 'Cliente desmarcou'),
(4, 3, 4, 4,    '2026-11-03', '14:00', 50.00, 'CONCLUIDO', NULL),
(5, 2, 3, NULL, '2026-11-05', '15:00', 60.00, 'AGENDADO',  NULL),
(1, 1, 1, NULL, '2026-11-06', '09:30', 40.00, 'AGENDADO',  NULL),
(2, 3, 5, NULL, '2026-11-06', '16:00', 15.00, 'AGENDADO',  NULL);
