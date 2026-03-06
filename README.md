# E-commerce System (Java Spring Boot)

This repository contains the backend and frontend implementation for an E-commerce system built with Java and Spring Boot. The primary focus of this application is a robust, scalable backend, paired with an intuitive and responsive front-end dashboard designed specifically for administrators.

## Project Overview

The core objective of this application is to furnish a comprehensive e-commerce platform. It handles essential functionalities through a cleanly orchestrated Model-View-Controller (MVC) architecture. Currently, the application implements the **Category Management Module**, satisfying robust backend logic requirements alongside clean frontend data visualization.

### Key Capabilities:
- **Category Management**: Easily `Create`, `Read`, `Update`, and `Deactivate/Delete` product categories.
- **Data Integrity Constraints**: A soft-delete process is enforced. When an admin attempts to deactivate a category, the system inherently blocks the process if there are still active *Products* assigned to that category—forcing a reassignment and maintaining data integrity.
- **Dynamic Frontend**: Modern layout powered by Bootstrap 5, complete with dynamic product badges (via Server-Side Rendering) to showcase how many products belong to each specific category in real-time.

---

## Technology Stack

### Backend
- **Java 17**: Core programming language.
- **Spring Boot 3 (3.2.x)**: Framework for dependency injection, embedded server configuration, and REST/MVC architecture.
- **Spring Data JPA & Hibernate**: Object-Relational Mapping (ORM) and data access layers.
- **H2 Database Engine**: Currently configured for rapid standalone testing (File-based embedded DB). Pre-configured properties for **MySQL** are available and easy to toggle within `application.properties`.

### Frontend
- **Thymeleaf**: Spring's recommended server-side Java template engine used to dynamically parse and render models from the backend.
- **Bootstrap 5**: A responsive CSS framework utilized to design clean, mobile-first administrative interfaces, layout sidebars, and grid systems.
- **FontAwesome 6**: Extensive icon libraries used for clear, visual navigation indicators.

---

## Getting Started

### Prerequisites
Before running the application locally, ensure you have the following installed on your machine:
- [Java Development Kit (JDK) 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Apache Maven](https://maven.apache.org/install.html) (Optional: Spring provides a Maven wrapper `mvnw` within the project, though you can use your global installation).
- *(Optional)* MySQL Server (if you intend to switch away from the default standalone H2 Database).

### Installation & Execution

1. **Clone the repository:**
   ```bash
   git clone https://github.com/jadhavjay2121-glitch/ECOMMERCE-SITE.git
   cd ecommerce-project
   ```

2. **Clean and Build the Project:**
   ```bash
   mvn clean install
   ```

3. **Run the Spring Boot Server:**
   ```bash
   mvn spring-boot:run
   ```
   *Note: Upon starting, Spring Data JPA (Hibernate) will automatically generate the database schema using the `update` DDL.*

4. **Access the Application:**
   Open a web browser and navigate to:
   ```text
   http://localhost:8080/admin/categories
   ```

---

## Configuration

### Database Tweaks
To switch from the default H2 database to MySQL, navigate to `src/main/resources/application.properties`. Comment out the H2 settings and uncomment the MySQL settings appropriately:

```properties
# MySQL DB Configuration (Example)
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

## Deployment Standards

In following deployment procedures, the completed built package (often a packaged JAR) can run anywhere Java is supported. To package the application for a live free-tier deployment server (like Render, Railway, or Heroku):

1. Generate the executable `.jar` file:
   ```bash
   mvn clean package
   ```
2. The generated output will be nested in the `/target/` folder as `ecommerce-system-0.0.1-SNAPSHOT.jar`. Run the package directly using standard Java:
   ```bash
   java -jar target/ecommerce-system-0.0.1-SNAPSHOT.jar
   ```

---

*This application is developed as part of an integrated internship project case study.*
