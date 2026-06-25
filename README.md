# RPA Data Validator & Persister (POC)

Esta é uma Prova de Conceito (POC) de uma aplicação backend robusta voltada para o ecossistema de automação de processos (RPA). O objetivo principal é demonstrar como tratar robôs de captura de dados como **softwares resilientes**, distanciando-se do modelo clássico de scripts procedurais frágeis.

A aplicação consome lotes de dados assíncronos (arquivos CSV), realiza a sanitização e validação de regras de negócio linha por linha e garante a persistência apenas de dados limpos em um banco de dados relacional, expondo os resultados através de uma API REST.

Arquitetura de Resiliência aplicada:

Defesa de I/O: O parser varre arquivos .csv de forma assíncrona.

Isolamento de Falha: Utilização de try-catch em escopo de linha. Se o dado de uma empresa vier corrompido, a transação daquela linha sofre rollback sem derrubar o processamento do lote restante.

Rastreabilidade: Implementação de Logs via SLF4J para auditoria de robôs de front-end.

## 🚀 Diferenciais de Arquitetura (Foco em Resiliência)

* **Isolamento de Falhas (Fault Tolerance):** Implementação de blocos de captura de exceção internos no loop de leitura de I/O. Se um registro específico do lote contiver dados corrompidos ou inválidos (como valores negativos ou campos obrigatórios ausentes), a transação daquela linha sofre rollback individual, mas o processamento do lote continua sem interromper a execução do robô.
* **Rastreabilidade (Logging):** Utilização do SLF4J/Logback para geração de logs em tempo real no console, permitindo auditoria detalhada de falhas de integração comuns em ambientes corporativos.
* **Mapeamento Objeto-Relacional Avançado:** Uso do Spring Data JPA e Hibernate para abstração da camada SQL, garantindo integridade referencial automatizada no banco de dados.

## 🛠️ Tecnologias Utilizadas

* **Java 17** (Linguagem base)
* **Spring Boot 3.x** (Framework de desenvolvimento)
* **Spring Data JPA** (Camada de persistência de dados)
* **MySQL** (Banco de dados relacional)
* **Maven** (Gerenciador de dependências e build)
* **Insomnia** (Para validação dos endpoints)


## 📁 Estrutura do Arquivo de Entrada (lote_pedidos.csv)

O robô está preparado para lidar com arquivos estruturados da seguinte forma, aplicando validações severas em campos vazios e valores monetários.

👨‍💻 Autor Desenvolvido por Mateus Castelani.
