# RevShop – Full Stack E-Commerce Web Application

RevShop is a full-stack e-commerce web application designed for both **buyers and sellers**.  
The platform enables buyers to browse products, manage shopping carts, place orders, and review purchases, while sellers can manage inventory, add products, track orders, and monitor stock levels.

The application is built using **Spring Boot, Hibernate, Oracle Database, and Thymeleaf**, with a layered architecture separating backend services and frontend web components.

---

# Project Overview

RevShop supports two primary user roles:

- **Buyer**
- **Seller**

Buyers can explore products, manage their carts, and place orders.  
Sellers can manage their inventory, monitor orders, and receive notifications.

The project is structured into **two Spring Boot applications**:

| Project | Description |
|------|-------------|
| revshop-api | Backend REST API for business logic and database access |
| revshop-web | Frontend web application using Thymeleaf templates |

The frontend communicates with the backend through **REST APIs**.

---

# Technology Stack

## Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven

## Frontend
- Thymeleaf
- HTML5
- CSS3
- Bootstrap
- JavaScript

## Database
- Oracle Database 21c Express Edition

## Tools
- IntelliJ IDEA / Spring Tool Suite
- Git & GitHub
- Oracle SQL Developer
- Postman

---

# System Architecture

The system follows a **client–server architecture**.

```
Browser
   │
   ▼
revshop-web (Frontend)
   │
   ▼
REST API Calls
   │
   ▼
revshop-api (Backend)
   │
   ▼
Oracle Database
```

Architecture diagram location:

```
docs/architecture/architecture-diagram.png
```

---

# Database Design

The application database is designed using relational modeling with multiple entities representing system components.

Main entities include:

- User
- Role
- Product
- Category
- Cart
- CartItem
- Order
- OrderItem
- Payment
- Review
- Notification

Entity Relationship Diagram:

```
docs/database/er-diagram.png
```

---

# Use Case Design

The system supports two actors:

- Buyer
- Seller

Key use cases include:

Buyer:
- Register and login
- Browse products
- Search products
- Add products to cart
- Checkout and place order
- Review purchased products

Seller:
- Register and login
- Add products
- Manage inventory
- View orders
- Monitor product reviews

Use Case Diagram location:

```
docs/usecases/usecase-diagram.png
```

---

# Project Structure

```
revshop-project
│
├── revshop
│   │
│   ├── revshop-api                # Backend Spring Boot Application
│   │   └── src/main/java/com/example/demo
│   │       ├── config
│   │       ├── controller
│   │       ├── entity
│   │       ├── repository
│   │       ├── service
│   │       ├── service/impl
│   │       ├── exception
│   │       └── RevshopApiApplication.java
│   │
│   │   └── src/main/resources
│   │       └── application.properties
│   │
│   │
│   └── revshop-web                # Frontend Spring Boot Application
│       └── src/main/java/com/example/demo
│           ├── config
│           ├── controller
│           ├── service
│           ├── service/impl
│           ├── dto
│           └── RevshopWebApplication.java
│
│       └── src/main/resources
│           ├── templates
│           │   ├── buyer
│           │   ├── seller
│           │   └── fragments
│           │
│           ├── static
│           │   ├── css
│           │   ├── js
│           │   └── images
│           │
│           └── application.properties
│
├── docs                          # Project Documentation
│   ├── architecture
│   │   └── architecture-diagram.png
│   │
│   ├── database
│   │   └── er-diagram.png
│   │
│   ├── usecases
│   │   └── usecase-diagram.png
│   │
│   └── screenshots
│
├── README.md
└── .gitignore
```

---

# Running the Application

## 1 Clone the Repository

```
git clone https://github.com/your-username/revshop-project.git
```

---

## 2 Configure Database

Update the database configuration in:

```
revshop/revshop-api/src/main/resources/application.properties
```

Example configuration:

```
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Ensure **Oracle Database 21c XE is running** before starting the application.

---

## 3 Run Backend Application

Navigate to the backend project:

```
cd revshop/revshop-api
```

Run the Spring Boot application:

```
mvn spring-boot:run
```

Backend will start at:

```
http://localhost:8080
```

---

## 4 Run Frontend Application

Navigate to frontend project:

```
cd revshop/revshop-web
```

Run the Spring Boot application.

The frontend will communicate with the backend using REST API calls.

---

# Project Modules

| Module | Description |
|------|-------------|
| Authentication & Authorization | User login and role-based access |
| Product & Category | Product catalog and category management |
| Cart | Shopping cart functionality |
| Order & Payment | Checkout and order processing |
| Review & Rating | Product reviews and ratings |
| Notification | Order alerts and system notifications |

---

# Documentation

Project documentation is available inside the **docs** folder.

| Folder | Description |
|------|-------------|
| docs/architecture | System architecture diagrams |
| docs/database | Entity relationship diagram |
| docs/usecases | Use case diagrams |

---

# Team Collaboration

The project was developed collaboratively using Git and GitHub.  
Each team member contributed to different modules while maintaining consistent coding standards and architecture.

Feature branches were used for development and later merged into the main branch after testing.

---

# Future Enhancements

- Online payment gateway integration
- Advanced product search and filtering
- Email notifications
- Inventory analytics dashboard
- Product recommendation system
