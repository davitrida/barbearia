# Barbearia – Sistema de Agendamento

Sistema desktop para gerenciar os atendimentos de uma barbearia: cadastro de clientes, profissionais, serviços e formas de pagamento, agendamento de horários e relatório de atendimentos por período.

Trabalho prático da disciplina de **Programação Orientada a Objetos**.

## Tecnologias

- **Java 21+** com **Swing** (JFrame) para a interface
- **MySQL 8+** como banco de dados
- **JDBC** (MySQL Connector/J) para a conexão com o banco
- **Maven** para o build e as dependências
- **Apache NetBeans** como IDE (telas feitas no editor visual)

## Arquitetura (MVC)

```
src/main/java/br/ucb/barbearia/
├── Barbearia.java   → ponto de entrada (abre a TelaPrincipal)
├── model/           → classes de domínio (Cliente, Agendamento...)
├── dao/             → acesso ao banco via JDBC (único lugar com SQL)
├── controller/      → validações e regras de negócio
└── view/            → telas Swing (só chamam o controller)
```

O caminho de uma ação é sempre: **View → Controller → DAO → MySQL**.

## Funcionalidades

| Tela | O que faz |
|---|---|
| Clientes | CRUD completo |
| Profissionais | CRUD completo, com opção de desativar |
| Serviços | CRUD completo, com preço e duração |
| Formas de Pagamento | CRUD completo, sem descrições repetidas |
| **Agendamentos** | Rotina principal: CRUD que relaciona cliente, profissional, serviço e forma de pagamento (chaves estrangeiras), com listagem via **JOIN** |
| Relatório | Atendimentos por período, com filtro de status e resumo (quantidades e total faturado) |

### Regras de negócio do agendamento

1. Cliente, profissional e serviço são obrigatórios.
2. Data e hora precisam ser válidas.
3. Um agendamento com status AGENDADO não pode ser para um horário que já passou.
4. O mesmo profissional não pode ter dois agendamentos ativos no mesmo dia e hora.
5. Para marcar como CONCLUIDO, a forma de pagamento é obrigatória.
6. O valor vem do preço do serviço e fica gravado no agendamento.

Além disso, o banco usa `ON DELETE RESTRICT`: não é possível excluir clientes, profissionais, serviços ou formas de pagamento que já estejam em agendamentos.

## Como executar

### 1. Pré-requisitos
- JDK 21 ou mais recente
- MySQL Server 8+ e MySQL Workbench
- Apache NetBeans

### 2. Criar o banco
Abra o arquivo `database/barbearia.sql` no MySQL Workbench e execute o script inteiro (ícone do raio).
Ele cria o banco `barbearia`, as 5 tabelas e os dados de demonstração.

> O script começa com `DROP DATABASE IF EXISTS`: executá-lo de novo apaga os dados e volta ao estado inicial.

### 3. Configurar o acesso ao banco
Em `src/main/resources/`, copie o arquivo `db.properties.exemplo` e renomeie a cópia para `db.properties`.
Depois, preencha a sua senha do MySQL:

```properties
db.url=jdbc:mysql://localhost:3306/barbearia?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
db.usuario=root
db.senha=SUA_SENHA_AQUI
```

O `db.properties` não vai para o GitHub (está no `.gitignore`), porque cada pessoa tem a própria senha.

### 4. Rodar
No NetBeans: **File → Open Project**, selecione a pasta do projeto e clique em **Run Project** (botão ▶️).
O Maven baixa o driver do MySQL automaticamente na primeira vez.

## Banco de dados

```
cliente (1) ──────< agendamento >────── (1) profissional
                     │       │
       servico (1) ──┘       └── (1) forma_pagamento
```

Todas as relações são 1:N. As chaves estrangeiras ficam na tabela `agendamento`.
A forma de pagamento é opcional até o atendimento ser concluído.

## Autores

- Davi Trida
- Fernando Cunha
- Guilherme Comaccio
- Felipe Ricardo Silva
- Gabriel Wolney Drumond
