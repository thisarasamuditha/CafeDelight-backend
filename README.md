# CafeDelight Backend (Spring Boot)

Backend service for CafeDelight that handles:
- user registration and login
- menu item listing
- order placement
- order detail retrieval
- user order history

## Tech Stack
- Java 17
- Spring Boot 4
- Spring Web
- Spring Data JPA
- MySQL
- Maven Wrapper (`mvnw`)

## Project Structure
- `src/main/java/com/thisara/cafe/controller` - REST controllers
- `src/main/java/com/thisara/cafe/service` - business logic
- `src/main/java/com/thisara/cafe/repository` - JPA repositories
- `src/main/java/com/thisara/cafe/entity` - JPA entities
- `src/main/java/com/thisara/cafe/dto` - request/response DTOs
- `src/main/java/com/thisara/cafe/exception` - global exception handling

## Prerequisites
- JDK 17+
- MySQL running locally
- Database: `cafe_db`

## Configuration
Update `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cafe_db
spring.datasource.username=<your_db_user>
spring.datasource.password=<your_db_password>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Run the Application
From the `backend` folder:

### Windows
```bash
.\mvnw.cmd spring-boot:run
```

### macOS/Linux
```bash
./mvnw spring-boot:run
```

App default URL: `http://localhost:8080`

## Build
```bash
.\mvnw.cmd clean package
```

## Main APIs

### User APIs
- `POST /api/users/register`
- `POST /api/users/login`

### Item APIs
- `GET /api/items`

### Order APIs
- `POST /api/orders`
- `GET /api/orders/{orderId}`
- `GET /api/users/{userId}/orders`

## Example Place Order Request
`POST /api/orders`

```json
{
  "userId": 1,
  "items": [
    { "itemId": 101, "quantity": 2 },
    { "itemId": 102, "quantity": 1 }
  ]
}
```

## Notes
- Order placement validates item availability and deducts stock.
- `@Transactional` is used in order placement logic for consistency.
- Errors are returned via global exception handler in a consistent JSON format.
