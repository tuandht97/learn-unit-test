# Ecommerce Shop Management System

Welcome to the Ecommerce Shop Management System repository! This individual project aims to create a robust Ecommerce application utilizing Java, Spring Boot, Hibernate, and various other technologies.

## Technologies Used

- **Java 20:** Leveraging the latest features of the Java programming language.
- **Spring Boot:** A powerful framework for building Java-based enterprise applications.
- **Hibernate:** An object-relational mapping framework for the Java programming language.
- **REST Controllers:** Utilized for building RESTful web services to handle HTTP requests.
- **JUnit and Mockito:** Employed for testing purposes, ensuring code reliability and functionality.
- **Mock MVC:** Used for testing Spring MVC controllers.
- **Basic SQL:** Fundamental SQL for database interactions.
- **Maven:** A build automation tool for managing the project's build lifecycle.
- **IntelliJ IDEA:** The integrated development environment used for coding and development.
- **Swagger:** Implemented for API documentation, making it easier for developers to understand and consume the API.
- **PostgreSQL:** Chosen as the primary relational database for robust data storage.
- **Liquibase:** Ensured version-controlled database schema updates and management.
- **Bitbucket:** The version control system utilized for source code management.
- **Pull Requests and Code Review:** Best practices for collaborative development and maintaining code quality.
- **GIT:** Version control system for tracking changes in the source code.

## Project Highlights

- **Swagger Documentation:** Implemented Swagger for clear and concise API documentation, facilitating easier integration and understanding.
- **Data Generator:** Incorporated a data generator for realistic testing scenarios, ensuring comprehensive testing of the application.
- **Global Exception Handling:** Established a global exception handling mechanism to enhance the application's resilience to unforeseen errors.
- **Efficient Database Management:** Utilized both SQL and PostgreSQL for efficient database querying and management.
- **Version-Controlled Database Schema:** Managed the database schema updates seamlessly using Liquibase.
- **Stream API:** Leveraged the Stream API for efficient data processing and manipulation in the application.

## Dependencies

Ensure you have the following dependencies installed on your development environment:

- **Java 20:** [Download and Install Java](https://www.oracle.com/java/technologies/javase-downloads.html)
- **Maven:** [Download and Install Maven](https://maven.apache.org/download.cgi)
- **PostgreSQL:** [Download and Install PostgreSQL](https://www.postgresql.org/download/)

## Installation

Follow these steps to set up and run the Ecommerce Shop Management System:

1. **Clone the repository:**
    ```bash
    git clone https://github.com/joannawalach1/Ecommerce-ShopManagmentSystem.git
    ```

2. **Navigate to the project directory:**
    ```bash
    cd Ecommerce-ShopManagmentSystem
    ```

3. **Build the project using Maven:**
    ```bash
    mvn clean install
    ```

4. **Create a PostgreSQL database:**
    - Create a PostgreSQL database named `ecommerce_db`.
    - Update the database configuration in the `application.properties` file if needed.

5. **Run Liquibase for database migrations:**
    ```bash
    mvn liquibase:update
    ```

6. **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

7. **Access the application:**
    Open your web browser and go to [http://localhost:8080](http://localhost:8080) to access the Ecommerce Shop Management System.
