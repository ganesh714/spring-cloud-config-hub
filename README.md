# Spring Cloud Config Hub

This project implements a centralized, Git-backed configuration server using Spring Cloud Config, and a dynamic client microservice (`inventory-service`) that consumes configurations dynamically.

## Architecture
- **Config Server**: A Spring Boot application providing centralized configurations backed by a local Git repository.
- **Inventory Service**: A Spring Boot client microservice that dynamically pulls its configurations (e.g., `dev` or `prod` profiles) and can refresh them with zero downtime using Spring Boot Actuator.
- **Config Repo**: A Git repository containing the configuration YAML files for different environments.

## Prerequisites
- Docker Desktop

## Setup and Execution Instructions

1. **Start the Infrastructure**
   From the root of this project, build and start the Docker containers:
   ```bash
   docker-compose up --build
   ```

2. **Verify the Endpoints**
   - **Config Server (Dev Properties):** `http://localhost:8888/inventory-service/dev`
   - **Inventory Service Config:** `http://localhost:8081/api/inventory/config`
   - **Inventory Service Healthcheck:** `http://localhost:8081/api/inventory/health`

3. **Dynamic Refresh Testing**
   To test zero-downtime configuration updates:
   - Make a change to `config-repo/inventory-service-dev.yml` (e.g. update `maxStock`).
   - Commit the change inside the `config-repo` directory (`git commit -am "Update maxStock"`).
   - Trigger the actuator refresh:
     ```bash
     curl -X POST http://localhost:8081/actuator/refresh
     ```
   - Check the Inventory Service Config endpoint again to see your changes live!
