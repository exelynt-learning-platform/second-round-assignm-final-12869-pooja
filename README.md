# E-commerce Backend System

## Features
- User Registration & Login (JWT Authentication)
- Product Management (CRUD)
- Cart Management
- Order Placement
- Payment Integration (Stripe)

##  Security
- JWT-based authentication
- Password encryption using BCrypt
- Role-based access (USER, ADMIN)

##  Tech Stack
- Java
- Spring Boot
- Spring Security
- MySQL
- Maven
- Postman

## Application Runs On
1. mvn spring-boot:run
2. http://localhost:8082

## API Endpoints
* Auth
-POST /auth/register
-POST /auth/login
* Products
-GET /products
-GET /products/{id}
-POST /products
-PUT /products/{id}
-Cart
-GET /cart
-POST /cart/add
-PUT /cart/update
-DELETE /cart/remove
* Orders
-POST /orders
-GET /orders
-GET /orders/{id}
*Payment
-POST /payment/checkout

## Testing
*Unit tests for:
-Authentication
-Cart Service
-Order Processing
-Payment Integration
