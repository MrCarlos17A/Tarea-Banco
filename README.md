# Prototipo en Java (Spring Boot 3 / Java 21) - caso BCP, uso académico
1. `cp .env.example .env`  (JWT_SECRET debe tener 32+ caracteres)
2. `docker compose up --build`  y abrir http://localhost:8080
3. Usuario inicial: admin@demo.pe / Cambiar123! (cambiar con ADMIN_PWD)
Pruebas: `cd backend && mvn test`
Endpoints: POST /api/login · POST /api/clientes · POST /api/solicitudes · GET /api/solicitudes · GET /actuator/health
Kubernetes: `kubectl create ns prod && kubectl apply -f k8s/` (ajustar imagen y crear secret backend-secrets).
