# Real-time transaction statistics

The main use case for our API is to
calculate realtime statistic from the last 60 seconds by default or other time interval if specified otherwise. There will be two APIs, one of them is
called every time a transaction is created. The other one
returns the real-time statistic for calculated transactions.

## Getting Started

### Building the service

`./mvnw clean install test`

### Running the service
`mvn spring-boot:run`

or

`docker-compose up --build`

### Running the tests

`mvn test`

## Available Endpoints

POST /transactions : Every Time a new transaction happened, this endpoint will be called.

GET /statistics    : Returns the statistic based on the transactions which happened in the last 60 seconds by default.

API documentation:

http://localhost:8080/swagger-ui.html

## Time and space complexity
O(1) for all endpoints.