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

Database: Any compatible database (MySQL)

Tools: Liquibase, HikariCP, JJWT, jBCrypt, Jackson Databind 


API Endpoints

You can interact with the system using these endpoints:

POST	/signup	Create a new user account.

POST	/login	Log in and receive a security token.

GET	/dashboard	View your profile and balance (requires security token).

POST	/deposit	Add funds to your account (requires security token).


POST	/withdraw	Take funds from your account (requires security token).


New Updates (Recent Additions)

1. Heavy Testing (JUnit & Mockito)

I wrote tests for almost everything to make sure the code doesn't break when I change things.

Model Tests: Making sure the User data stays correct.

DAO Tests: I used Mockito to mock the DataSource and ResultSet. This lets me test database code (like getUserByUsername and updateUserBalance) without needing a real database running.

Servlet Tests: I added tests for LoginServlet, SignupServlet, DepositServlet, and WithdrawServlet. They simulate real JSON requests and check if the API returns "Success" or "Failure" correctly.

2. Better Logging

I moved away from simple print statements. Now the app uses a Logging framework to track user actions and errors. It shows the time and what happened (INFO for normal stuff, ERROR for bugs), which makes it much easier to fix problems.

3. Docker & Docker Compose

The project is now fully containerized using two separate parts:

App Image: I created a Dockerfile that packages my Java code, Tomcat, and all my tools (Liquibase, etc.) into one image.

Database Image: I use a second image for the database (MySQL).

Docker Compose: I created a docker-compose.yml file so you can start the entire system (the API + the Database) with just one command:
docker-compose up --build

This makes sure the API and the DB can talk to each other automatically without any manual setup.
