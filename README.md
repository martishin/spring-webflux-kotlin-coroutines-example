# User Management Service
REST API service used for management of users and companies, along with their interconnections. 

## Endpoints
[Online Postman collection](https://documenter.getpostman.com/view/19365947/2s9YeD7sov)

## 🚀 Running Locally
### Prerequisites
Set up a local PostgreSQL database: `docker compose up db`
  
### Starting the server
Launch the server with: `./gradlew bootRun`

### Testing
Execute tests via: `./gradlew test`

## ⚙️ Technologies Used
- [Kotlin](https://kotlinlang.org/) with [coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Gradle](https://gradle.org/)
- [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [PostgreSQL](https://www.postgresql.org/)
- [Spring Data R2DBC](https://spring.io/projects/spring-data-r2dbc)
- [Flyway](https://flywaydb.org/)
- [Jooq](https://www.jooq.org/)
- [Testcontainers](https://testcontainers.com/)
- [Logback](https://logback.qos.ch/)
- [Docker](https://www.docker.com/)
- [Micrometer](https://micrometer.io/)
- [Prometheus](https://prometheus.io/)
