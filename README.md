# Ecommerce Backend Project
  Java Spring Boot backend for an ecommerce application.  
  It provides features like user authentication, product management, cart management, order processing, and payment handling using Stripe.
# Project Structure
- `src/main/java` – Java source code
  - `controller` – REST API controllers
  - `service` – Business logic
  - `repository` – Database access
  - `entity` – JPA entities
  - `security` – JWT and security configurations
  - `dto` – Data Transfer Objects
  - `exception` – Custom exception handling
- `src/main/resources` – Configuration files
- `src/test/java` – Unit and integration tests
- `pom.xml` – Maven project file

# Prerequisites
  - Java 11 or higher  
  - Maven  
  - Postman  
  - Stripe account (for payment testing)

# Stripe API Key Setup
  The project uses Stripe for payments. The key in the repository is a **placeholder**, so the project won’t work until you add a real test key.
   Steps:
        1. Sign up at [Stripe](https://stripe.com) and get a **test API key**.
        2. Paste your real test key directly in `application.properties`
          : stripe.api.key=YOUR_TEST_KEY_HERE

# Setup & Run Instructions : 
 * Clone the repository: 
    git clone https://github.com/exelynt-learning-platform/second-round-assignm-final-12869-pooja.git
    cd second-round-assignm-final-12869-pooja
 * Build the project :
    mvn clean install
* Run the backend :
    mvn spring-boot:run
    The backend will run at: http://localhost:8081
    Test APIs using Postman
    Test Cases included for CartService, OrderService, PaymentService, ProductService, and AuthService.

# Postman API Test Screenshot
The screenshot below shows sample API requests and responses using Postman:
 ![Postman Test 1](images/postman-test1.png)
![Postman Test 2](images/postman-test2.png)

## Author
  Pooja – Second-round assignment submission
