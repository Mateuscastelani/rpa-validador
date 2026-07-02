🤖 RPA Data Validator & Persister (POC)

Uma API RESTful desenvolvida em Java 17 e Spring Boot 3, concebida como uma Prova de Conceito (POC) para sistemas de apoio a robôs de Automação de Processos Robóticos (RPA).

O foco central desta aplicação é demonstrar resiliência, tolerância a falhas e integridade de dados. Diferente de scripts procedurais que falham catastroficamente ao encontrar dados anômalos, esta API é desenhada para isolar sujeiras em lotes de dados (I/O), processar as informações válidas transacionalmente e manter um histórico confiável para auditoria.

🏗️ Decisões Arquiteturais e Design Patterns

Para alinhar a aplicação com os padrões corporativos do mercado de RPA, as seguintes abordagens foram implementadas:

Fault Tolerance e Isolamento em I/O: A leitura de arquivos em lote possui tratamento de exceção em escopo de linha. Se um registro estiver corrompido, a transação local sofre rollback e é logada, mas o processamento do lote continua ileso.

Soft Delete (Exclusão Lógica): Atendendo a requisitos de compliance e auditoria, registros nunca são apagados fisicamente do banco de dados (MySQL). A exclusão altera o estado do registro para inativo (ativo = false), ocultando-o da listagem principal sem destruir a rastreabilidade.

Safe Patch (Atualização Parcial Segura): A rota de atualização (PUT) possui validação condicional. Dados obrigatórios vazios ou valores numéricos inválidos são barrados antes de tocar no banco de dados, protegendo a entidade contra corrupção tardia.

🚀 Tecnologias Utilizadas

Java 17

Spring Boot 3.x (Web, Data JPA)

MySQL 8 (Banco de dados relacional)

Maven (Gerenciamento de dependências)

📚 Documentação dos Endpoints (API Reference)

Base URL: http://localhost:8080/api/robo

1. Processar Lote de Dados

Gatilho manual para que o robô faça a varredura, validação e persistência de um arquivo CSV de entrada.

Método: POST

Endpoint: /processar-lote

Corpo da Requisição: None (A API lê o arquivo lote_pedidos.csv alocado na raiz do projeto).

Resposta de Sucesso (200 OK):
Retorna um relatório de execução (RelatorioExecucao) no formato JSON, vital para orquestradores de RPA monitorarem a saúde da automação.

{
  "totalLido": 5,
  "salvosComSucesso": 2,
  "falhas": 3,
  "logErros": [
    "Erro na linha 2 (PED-1002): Valor inválido: -400.00. O preço deve ser maior que zero.",
    "Erro na linha 3 (PED-1003): O nome do produto veio em branco."
  ]
}


2. Listar Pedidos Processados

Recupera todos os pedidos que foram higienizados e salvos com sucesso pelo robô. (Nota: Graças ao mecanismo de Soft Delete, apenas registros com a flag ativo: true são retornados).

Método: GET

Endpoint: /processados

Corpo da Requisição: None

Resposta de Sucesso (200 OK):

[
  {
    "id": 1,
    "codigo": "PED-1001",
    "cliente": "Empresa Alfa",
    "produto": "Licença Windows XP",
    "valor": 1500.50,
    "ativo": true
  }
]


3. Atualizar Pedido (Safe Patch)

Atualiza as informações de um pedido específico. Aplica validações de regras de negócio antes de realizar a persistência para garantir que dados nulos não sobrescrevam dados íntegros.

Método: PUT

Endpoint: /pedidos/{id}

Parâmetro de Rota: id (Long) - ID do pedido no banco de dados.

Corpo da Requisição (JSON):

{
  "codigo": "PED-1001-ALT",
  "cliente": "Empresa Alfa Atualizada",
  "produto": "Licença Windows 11 Pro",
  "valor": 2100.00
}


Respostas Possíveis:

200 OK: Retorna o objeto atualizado.

400 Bad Request: Caso as regras de negócio sejam violadas (ex: valor negativo).

404 Not Found: Caso o ID não exista no banco.

4. Exclusão Lógica de Pedido

Remove um pedido da esteira de operação sem destruí-lo do banco de dados (Soft Delete).

Método: DELETE

Endpoint: /pedidos/{id}

Parâmetro de Rota: id (Long) - ID do pedido a ser removido.

Corpo da Requisição: None

Respostas Possíveis:

204 No Content: Exclusão lógica realizada com sucesso (Status ativo alterado para false). A API não retorna corpo nesta resposta, seguindo o padrão arquitetural REST.

404 Not Found: Caso o ID informado não exista na base.

🛠️ Como Executar o Projeto Localmente

Clone o repositório:

git clone [https://github.com/seu-usuario/rpa-validador-dados.git](https://github.com/seu-usuario/rpa-validador-dados.git)
cd rpa-validador-dados


Configure o Banco de Dados MySQL:
Abra o seu cliente MySQL e crie o schema:

CREATE DATABASE rpa_demo;


Configure as Credenciais de Acesso:
No arquivo src/main/resources/application.properties, ajuste as credenciais do seu banco local:

spring.datasource.username=seu_usuario_mysql
spring.datasource.password=sua_senha_mysql


Execute a Aplicação:
Via linha de comando com Maven:

mvn spring-boot:run


A API estará escutando e pronta para receber requisições na porta 8080.

Desenvolvido como demonstração técnica de proficiência em Backend e engenharia de software para RPA.

👨‍💻 Autor
Desenvolvido por Mateus Castellani
👨‍💻 Autor Desenvolvido por Mateus Castellani.
