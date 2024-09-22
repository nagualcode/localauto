# LocAuto

## Introdução

LocAuto é um sistema distribuído baseado em microserviços para a gestão de aluguel de carros, com um forte foco em **Design Orientado a Domínio (DDD)** e uma **Arquitetura Baseada em Eventos (Event-Driven)**. O sistema integra três serviços principais:

1. **Serviço de Clientes** (`clientes`)
2. **Serviço de Carros** (`carros`)
3. **Serviço de Locações** (`locacoes`)

A interação entre esses serviços é facilitada por meio do **RabbitMQ**, tornando o sistema assíncrono e baseado em eventos, onde as mensagens são trocadas em tempo real sem acoplamento rígido entre os serviços.

## Visão Geral da Arquitetura

### Design Orientado a Domínio (DDD)

Cada microserviço no sistema corresponde a um **Bounded Context** no DDD, sendo responsável pela gestão de seu próprio modelo de domínio:

- **Serviço de Clientes (`clientes`)**: Gerencia o ciclo de vida dos clientes, incluindo registro, atualizações de perfil e exclusão.
- **Serviço de Carros (`carros`)**: Gerencia o ciclo de vida dos veículos, com detalhes como modelo, marca e status de disponibilidade.
- **Serviço de Locações (`locacoes`)**: Responsável pelo processo de aluguel de carros, incluindo a criação, atualização e finalização das locações. Ele também se integra com o serviço de carros para validar a disponibilidade de veículos antes de iniciar um aluguel.

### Arquitetura Baseada em Eventos (EDA)

A arquitetura baseada em **eventos** permite que os serviços se comuniquem de forma assíncrona. Eventos como "Cliente Criado" ou "Carro Alugado" são publicados e assinados por outros serviços que precisam reagir a essas mudanças. Isso desacopla os serviços, permitindo que eles evoluam independentemente e respondam a eventos sem depender de chamadas síncronas diretas.

#### Interação Entre os Microserviços

1. **Clientes**: O serviço de clientes publica eventos como `ClienteCriado` quando um novo cliente é registrado.
   
2. **Carros**: O serviço de carros escuta eventos relacionados à disponibilidade de veículos e emite eventos como `CarroDisponivel` e `CarroIndisponivel`.

3. **Locações**: O serviço de locações coordena o processo de aluguel e publica eventos como `LocacaoIniciada` e `LocacaoFinalizada`. Ele consome eventos de disponibilidade de carros para iniciar ou cancelar uma locação, caso um carro não esteja disponível.

### RabbitMQ

O **RabbitMQ** é o mecanismo central de comunicação no sistema, sendo utilizado para publicar e assinar eventos entre os microserviços. Cada serviço possui filas dedicadas para garantir a escalabilidade e o desacoplamento, permitindo que eles processem eventos em seus próprios tempos e sem sobrecarregar o sistema.

### Endpoints e Comandos cURL para Testar o Sistema

#### **Serviço de Clientes**

1. **Cadastrar Cliente:**
   - **Endpoint:** `POST /api/clientes/cadastro`
   - **Descrição:** Cadastra um novo cliente.
   - **cURL:**
   ```bash
   curl -X POST http://localhost:8081/api/clientes/cadastro \
   -H "Content-Type: application/json" \
   -d '{
         "nome": "João Silva",
         "telefone": "999999999",
         "cpf": "12345678900",
         "nascimento": "1980-01-01"
       }'
   ```

2. **Atualizar Cliente:**
   - **Endpoint:** `PUT /api/clientes/{id}`
   - **Descrição:** Atualiza os dados de um cliente específico.
   - **cURL:**
   ```bash
   curl -X PUT http://localhost:8081/api/clientes/1 \
   -H "Content-Type: application/json" \
   -d '{
         "nome": "João Silva Atualizado",
         "telefone": "888888888",
         "cpf": "12345678900",
         "nascimento": "1980-01-01"
       }'
   ```

3. **Listar Todos os Clientes:**
   - **Endpoint:** `GET /api/clientes`
   - **Descrição:** Retorna a lista de todos os clientes cadastrados.
   - **cURL:**
   ```bash
   curl -X GET http://localhost:8081/api/clientes
   ```

4. **Consultar Cliente por ID:**
   - **Endpoint:** `GET /api/clientes/{id}`
   - **Descrição:** Retorna as informações de um cliente específico.
   - **cURL:**
   ```bash
   curl -X GET http://localhost:8081/api/clientes/1
   ```

5. **Excluir Cliente:**
   - **Endpoint:** `DELETE /api/clientes/{id}`
   - **Descrição:** Exclui um cliente específico.
   - **cURL:**
   ```bash
   curl -X DELETE http://localhost:8081/api/clientes/1
   ```

---

#### **Serviço de Carros**

1. **Adicionar Carro:**
   - **Endpoint:** `POST /api/carros`
   - **Descrição:** Cadastra um novo carro.
   - **cURL:**
   ```bash
   curl -X POST http://localhost:8082/api/carros \
   -H "Content-Type: application/json" \
   -d '{
         "modelo": "Fiat Uno",
         "marca": "Fiat",
         "placa": "ABC-1234",
         "cor": "Branco"
       }'
   ```

2. **Atualizar Carro:**
   - **Endpoint:** `PUT /api/carros/{id}`
   - **Descrição:** Atualiza os dados de um carro específico.
   - **cURL:**
   ```bash
   curl -X PUT http://localhost:8082/api/carros/1 \
   -H "Content-Type: application/json" \
   -d '{
         "modelo": "Fiat Uno Atualizado",
         "marca": "Fiat",
         "placa": "DEF-5678",
         "cor": "Preto"
       }'
   ```

3. **Listar Todos os Carros:**
   - **Endpoint:** `GET /api/carros`
   - **Descrição:** Retorna a lista de todos os carros cadastrados.
   - **cURL:**
   ```bash
   curl -X GET http://localhost:8082/api/carros
   ```

4. **Consultar Carro por ID:**
   - **Endpoint:** `GET /api/carros/{id}`
   - **Descrição:** Retorna as informações de um carro específico.
   - **cURL:**
   ```bash
   curl -X GET http://localhost:8082/api/carros/1
   ```

5. **Excluir Carro:**
   - **Endpoint:** `DELETE /api/carros/{id}`
   - **Descrição:** Exclui um carro específico.
   - **cURL:**
   ```bash
   curl -X DELETE http://localhost:8082/api/carros/1
   ```

---

#### **Serviço de Locações**

1. **Iniciar Locação:**
   - **Endpoint:** `POST /api/locacoes`
   - **Descrição:** Inicia uma nova locação.
   - **cURL:**
   ```bash
   curl -X POST http://localhost:8083/api/locacoes \
   -H "Content-Type: application/json" \
   -d '{
         "clienteId": 1,
         "carroId": 1
       }'
   ```

2. **Finalizar Locação:**
   - **Endpoint:** `PUT /api/locacoes/{id}/fimlocacao`
   - **Descrição:** Finaliza uma locação e marca como paga.
   - **cURL:**
   ```bash
   curl -X PUT http://localhost:8083/api/locacoes/1/fimlocacao
   ```

3. **Listar Todas as Locações:**
   - **Endpoint:** `GET /api/locacoes`
   - **Descrição:** Retorna a lista de todas as locações, com a opção de filtrar por locações pagas.
   - **cURL:**
   ```bash
   curl -X GET http://localhost:8083/api/locacoes?pago=true
   ```

4. **Consultar Locação por ID:**
   - **Endpoint:** `GET /api/locacoes/{id}`
   - **Descrição:** Retorna as informações de uma locação específica.
   - **cURL:**
   ```bash
   curl -X GET http://localhost:8083/api/locacoes/1
   ```

5. **Consultar Locações por Cliente:**
   - **Endpoint:** `GET /api/locacoes/cliente/{clienteId}`
   - **Descrição:** Retorna todas as locações feitas por um cliente.
   - **cURL:**
   ```bash
   curl -X GET http://localhost:8083/api/locacoes/cliente/1
   ```

6. **Consultar Locações por Carro:**
   - **Endpoint:** `GET /api/locacoes/carro/{carroId}`
   - **Descrição:** Retorna todas as locações feitas para um carro específico.
   - **cURL:**
   ```bash
   curl -X GET http://localhost:8083/api/locacoes/carro/1
   ```

