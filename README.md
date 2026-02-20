# Order Management Microservices

This project is a microservices-based order management system built with Spring Boot, Spring Cloud, and PostgreSQL.

## Features

### 1. Load Balancing
Load balancing is implemented using Spring Cloud LoadBalancer.
- **Load Balance Testing**: The test cases written for the `order-service` to verify load balancing capabilities are applicable across all microservices in this project.
- The system automatically handles service discovery and client-side load balancing via Eureka and API Gateway.

### 2. Dockerization
The entire application ecosystem is fully dockerized.
- Each service contains its own `Dockerfile`.
- A root `docker-compose.yaml` is provided to orchestrate all services, including the database (PostgreSQL), Config Server, Discovery Service, and all functional microservices.
- **Usage**: Run `docker-compose up --build` to start the entire system.

### 3. Role-Based Access Control (RBAC)
Security is enforced at the API Gateway level using JWT authentication.
- **Admin-Only Functions**: Certain critical operations are restricted to users with the **ADMIN** role.
    - **Product Management**: Creating, updating, or deleting products/categories.
    - **Inventory Management**: Updating stock levels.
- Unauthorized attempts to access these endpoints by users without the ADMIN role will result in a `403 Forbidden` error.

## Microservices Architecture

- **Config Server**: Centralized configuration management.
- **Discovery Service**: Eureka-based service registration and discovery.
- **API Gateway**: Single entry point with JWT authentication and routing.
- **User/Auth Service**: User management and security.
- **Product Service**: Catalog management.
- **Inventory Service**: Stock tracking.
- **Order Service**: Order processing and fulfillment.
