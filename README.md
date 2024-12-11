
# Real Time Ticketing System

This Real-Time Ticketing System is designed to handle concurrent access to a ticket pool with multiple threads, ensuring thread safety and smooth operation. It simulates real-time demand for tickets, making it suitable for high-demand environments such as event ticket sales.

The system consists of three main components:

**1. Front-End**: Built using Angular 18, providing a user-friendly interface for customers and vendors to interact with the ticket pool.

**2. Back-End**: Developed in Spring Boot, managing the core logic, including ticket pool operations, user authentication, and data processing.

**3. Command Line Interface (CLI)**: A tool for simulating multiple threads attempting to access the ticket pool, allowing for real-time demand simulation from the user’s perspective.

The system is designed to handle concurrent ticket requests in a thread-safe manner, ensuring no race conditions occur when multiple users access the system simultaneously.


## Features

- Event ticket creation and management
- Real-time updates
- Persistent storage with H2 Database
- Java CLI for backend operations
- REST API documentation with Swagger UI



## System Requirements

### 1. Backend (Spring Boot)

Environment:

- Java Development Kit (JDK): Java 22 (ensure JAVA_HOME is set)
- Maven: Version 3.8.1 or higher

Dependencies (from pom.xml):
- Spring Boot 3.3.5
- Spring Boot Starter Web (for REST API)
- Spring Boot Starter Data JPA (for database access)
- Spring Boot DevTools (for live reloading)
- Spring Boot Starter Test (for testing)
- H2 Database (for embedded database)
- Gson 2.10.1 (for JSON processing)
- Spring Boot Starter WebSocket (for real-time communication)

Database Configuration (from application.properties):
- Database: H2 File-based Database (./data/mydb)
- H2 Console URL: http://localhost:8080/h2-console
- Database Access:
    -  JDBC URL: jdbc:h2:file:./data/mydb
    - Username: sa
    - Password: (no password)

JPA and Logging Settings:
- DDL Auto: update (updates schema automatically)
- Hibernate Dialect: org.hibernate.dialect.H2Dialect
- Log File: logger/log.txt

Tools Required:
- IDE: IntelliJ IDEA / Eclipse

### 2. Frontend (Angular)

Environment:

- Node.js: Version 18.x or higher
- NPM: Version 8.x or higher
- Angular CLI: Version 15.x or higher

Dependencies (from angular.json):

- Angular Core
- Bootstrap 5 (for UI styling)
- Ngx-Toastr (for notifications)
- Zone.js (for change detection)

Tools Required:
- IDE: Visual Studio Code / WebStorm
## Project Setup & Installation

**Clone the repository:**

```bash
   git clone https://github.com/strava24/Real-Time-Event-Ticketing-System-.git
```


**Run the Backend:**
```bash
   cd TicketingSystem
   mvn spring-boot:run
```
Backend runs on: http://localhost:8080


**Run the Frontend.**
```bash
   cd angular-GUI
   npm install
   ng serve --port 4209
```
Frontend runs on: http://localhost:4209

The frontend runs on port 4209 to avoid CORS policy issues when interacting with the backend running on port 8080.

**Run the Command Line Interface.**

```bash
   cd CLI-TicketingSystem\src\Main.java
   javac Main.java
   java Main
```
## API Documentation

Swagger UI: http://localhost:8080/swagger-ui.html

## Contributing

Contributions are welcome! Please submit pull requests or issues on the repository.
