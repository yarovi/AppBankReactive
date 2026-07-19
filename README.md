# Con el perfil dev (por defecto si está en application.yml)
mvn spring-boot:run

# O especificando el perfil en la línea de comandos
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# usgin jar
java -jar target/bank-transfer-api-1.0.0.jar --spring.profiles.active=dev

# Primero obtén los IDs de las cuentas de MongoDB Compass
# Luego haz la transferencia
curl -X POST http://localhost:8080/api/v1/transfers/process \
-H "Content-Type: application/json" \
-d '{
"sourceAccountId": "6a5c3e161c17d44ec0bc2e5a",
"destinationAccountId": "6a5c3e161c17d44ec0bc2e5b",
"amount": 100.00,
"description": "Pago de prueba 2"
}'

# Transferencia con monto negativo
curl -X POST http://localhost:8080/api/v1/transfers/process \
-H "Content-Type: application/json" \
-d '{
"sourceAccountId": "6a5c3e161c17d44ec0bc2e5a",
"destinationAccountId": "6a5c3e161c17d44ec0bc2e5b",
"amount": -100.00,
"description": "Monto negativo"
}' | jq '.'

# Transferencia saldo insuficiente
# Asumiendo que la cuenta origen tiene 10,000 y tratamos de transferir 20,000
curl -X POST http://localhost:8080/api/v1/transfers/process \
-H "Content-Type: application/json" \
-d '{
"sourceAccountId": "6a5c3e161c17d44ec0bc2e5a",
"destinationAccountId": "6a5c3e161c17d44ec0bc2e5b",
"amount": 20000.00,
"description": "Monto mayor al saldo"
}' | jq '.'

# Reemplaza {transactionId} con el ID obtenido de la transferencia anterior
curl -X GET http://localhost:8080/api/v1/transfers/status/6a5c412e1c17d44ec0bc2e5c | jq '.'

# Reemplaza {accountId} con el ID de la cuenta origen
curl -X GET "http://localhost:8080/api/v1/transfers/report?accountId=ACC001&startDate=2024-01-01&endDate=2024-12-31" | jq '.'