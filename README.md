MiniBank Backend API

A robust, lightweight Java-based REST API for banking operations. This project utilizes Java Servlets, HikariCP for high-performance connection pooling, BCrypt for security, and Liquibase for database version control. 

Features

Stateless Authentication: Secure JWT-based login system.

Security: Password hashing using jBCrypt and a custom JWT Filter for route protection.

Banking Operations: User registration, account dashboard, deposits, and withdrawals with balance validation.

High Performance: Database connection pooling via HikariCP.

Database Versioning: Automated schema creation and updates using Liquibase. 

🛠 Tech Stack

Runtime: Java 8+
Server: Apache Tomcat 9.0+
Database: Any JDBC-compliant DB (MySQL/PostgreSQL)
Migration: Liquibase
Pooler: HikariCP
Security: JJWT (Java JWT), jBCrypt
JSON: Jackson Databind 

📂 API Endpoints

Public Endpoints

POST	/signup	Registers a new user (Requires: username, password, fullName, email).
POST	/login	Validates credentials and returns a JWT token.

Protected Endpoints (Requires Authorization: Bearer <token>) 

GET	/dashboard	Returns current user's profile and account balance.
POST	/deposit	Adds funds. Query Param: ?amount=X.XX
POST	/withdraw	Deducts funds. Query Param: ?amount=X.XX (Validates against balance).

⚙️ Configuration

Create a db.properties file in src/main/resources: 

# Database Configuration
jdbc.url=jdbc:mysql://localhost:3306/minibank
jdbc.username=root
jdbc.password=yourpassword
jdbc.driverClassName=com.mysql.cj.jdbc.Driver

# JWT Configuration
jwt.secret=your_very_long_random_string_for_security_minimum_256_bits

jwt.expiration.ms=3600000
