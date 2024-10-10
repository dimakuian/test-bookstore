Bookstore Project
This is a test interview project for a simple Bookstore application, built using Spring Boot. The application demonstrates the following key features:

- Spring Security: Secures endpoints with JWT-based authentication.
- Bookstore API: Manages books and authors.
- Currency Exchange: Allows dynamic price calculation using a currency exchange API.

Features:
- JWT Authentication: Secures API endpoints using JWT tokens. Users must log in to get a token.
- CRUD Operations: Full support for managing books and authors (Create, Read, Update, Delete).
- Fixer API Integration: Book prices can be displayed in different currencies by integrating with an external exchange rate service.
- H2 Database: Embedded database for rapid testing and development.
- Unit and Integration Testing: Includes tests using MockMvc to verify controller functionality and service-layer logic.

Tech Stack:
- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Spring Web
- MySQL 
- H2 Database (for testing)
- Mockito for mocking dependencies
- JUnit 5 for testing
- Fixer API: Integrated third-party API to retrieve exchange rates.
