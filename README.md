# Myretail-Products

The primary purpose of this microservice application is to make product details of myRetail organization available for all clients from myRetail.com to native mobile apps.
This application is primarily build of spring-boot framework with multiple ReST endpoints for 
1. Serve product information by productId
2. Update price information in the myRetail database for existing products

## Local Setup

### Prerequisite
 1. Docker (https://www.docker.com/products/docker-desktop)
 2. MongoDB compass (optional)
### How to run application locally
 1. Clone the application from repository to local workspace using (git clone https://github.com/an00pvijayan/myretail-products.git)
 2. Start Docker and initialize MongoDB backend (docker-compose up -d)
 3. Build the application using mvn clean build
 4. Execute the application by either running the ProductApplication class directly or execute `java -jar service/target/myretail-products-service-<version>.jar`
 5. Access endpoint using swagger-ui http://localhost:8080/swagger-ui/#/product-controller

## Development notes
