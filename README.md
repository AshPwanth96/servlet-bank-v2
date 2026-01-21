Overview

The MiniBank Backend API is a lightweight Java-based REST API designed for basic banking operations. It uses a modern tech stack to ensure performance and security. 


Key Features

Secure Access: Uses JWT (JSON Web Tokens) for a secure, stateless login system.

Strong Security: Protects passwords with jBCrypt hashing and secures routes with a custom JWT filter.

Core Banking Functions: Allows users to register, view their balance, deposit funds, and withdraw money (with checks to prevent overdrawing).

Fast Performance: Employs HikariCP for efficient database connection pooling.

Easy Database Setup: Uses Liquibase for automatic database schema management. 


Technology Used

Language: Java 8+

Server: Apache Tomcat 9.0+

Database: Any compatible database (MySQL/PostgreSQL)

Tools: Liquibase, HikariCP, JJWT, jBCrypt, Jackson Databind 


API Endpoints

You can interact with the system using these endpoints:

POST	/signup	Create a new user account.

POST	/login	Log in and receive a security token.

GET	/dashboard	View your profile and balance (requires security token).

POST	/deposit	Add funds to your account (requires security token).


POST	/withdraw	Take funds from your account (requires security token).
