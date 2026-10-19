# Dispatcher Service

The event-driven dispatch worker for Polar Bookshop. It consumes accepted orders from RabbitMQ, simulates packing and labeling, and publishes dispatch confirmation events. It uses Spring Cloud Stream with the RabbitMQ binder and Reactor.

## Messaging workflow

| Binding | Destination | Payload |
| --- | --- | --- |
| `packlabel-in-0` | `order-accepted` | `{"orderId": 123}` |
| `packlabel-out-0` | `order-dispatched` | `{"orderId": 123}` |

The configured function composition is `pack|label`. The `pack` function logs packing and extracts the order ID; `label` creates an `OrderDispatchedMessage`. The input consumer group is `dispatcher-service`.

This service simulates fulfillment; it does not integrate with a shipping provider. Order Service consumes the result and updates its database.

## Run locally

Start RabbitMQ using the [project setup](https://github.com/Aymanhamad2002/edge-service#run-locally), then run:

```powershell
.\gradlew.bat bootRun
```

The HTTP server listens on `http://localhost:9003` for operational endpoints, including `/actuator/health` and `/actuator/prometheus`. Business processing happens through RabbitMQ rather than a REST dispatch API. Run Catalog Service and Order Service to exercise the full ordering workflow.

| Environment override | Default |
| --- | --- |
| `SPRING_RABBITMQ_HOST` | `localhost` |
| `SPRING_RABBITMQ_PORT` | `5672` |
| `SPRING_RABBITMQ_USERNAME` / `SPRING_RABBITMQ_PASSWORD` | Local demo broker credentials |

## Related repositories

| Repository | Responsibility |
| --- | --- |
| [edge-service](https://github.com/Aymanhamad2002/edge-service) | Gateway, login, sessions, and full project overview |
| [catalog-service](https://github.com/Aymanhamad2002/catalog-service) | Book catalog and PostgreSQL persistence |
| [order-service](https://github.com/Aymanhamad2002/order-service) | Orders and asynchronous dispatch tracking |
| [dispatcher-service](https://github.com/Aymanhamad2002/dispatcher-service) | Event-driven packing and labeling |
| [polar-deployment](https://github.com/Aymanhamad2002/polar-deployment) | Docker Compose, Kubernetes, and observability configuration |
| [config-service](https://github.com/Aymanhamad2002/config-service) | Spring Cloud Config server |
| [config-repo](https://github.com/Aymanhamad2002/config-repo) | Shared application configuration |

## Build and test

Java 17 is configured in the Gradle toolchain. Use the included Gradle wrapper:

```powershell
.\gradlew.bat test
.\gradlew.bat bootJar
.\gradlew.bat bootBuildImage
```

On macOS/Linux, use `./gradlew` instead. Building container images requires Docker. Catalog and order tests include database integration tests; have Docker available for Testcontainers.

## Development configuration

The values in `src/main/resources/application.yml` are local development defaults. Demo credentials such as `user` / `password` are public, disposable values for local infrastructure; never reuse them for shared environments or real data.

Spring Boot allows environment variables to override YAML values without changing the file. For any shared or production environment, supply unique credentials externally through deployment configuration or a secret manager, and do not commit them.

See [Spring Boot externalized configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html) and [OWASP secrets management](https://cheatsheetseries.owasp.org/cheatsheets/Secrets_Management_Cheat_Sheet.html).

