# Sistema de Votação

API REST para gerenciar votos com banco de dados H2

## Como executar
mvn clean install

mvn spring-boot:run

Endpoints pode ser testados no Swagger: http://localhost:8080/swagger-ui.html

## Exemplos

Criar pauta: POST http://localhost:8080/api/v1/pautas 

Abrir sessão: http://localhost:8080/api/v1/sessoes

Registrar voto: http://localhost:8080/api/v1/votos

ver resultado: http://localhost:8080/api/v1/votos/resultado/1
