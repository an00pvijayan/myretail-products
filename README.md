# Myretail-Products

The primary purpose of this microservice application is to make product details of myRetail organization available for all clients from myRetail.com to native mobile apps.
This application is primarily build of spring-boot framework with multiple ReST endpoints for 
1. Serve product information by productId
2. Update price information in the myRetail database for existing products

## Local Setup

### Prerequisite
 1. Docker (https://www.docker.com/products/docker-desktop)
### How to run application locally
 1. Clone the application from repository to local workspace using (git clone https://github.com/an00pvijayan/myretail-products.git)
 2. Start Docker and initialize MongoDB backend (`docker-compose up -d`). This will also start mongo-express client
 3. Build the application using `mvn clean install`
 4. Execute the application by either running the ProductApplication class directly or execute `java -jar service/target/myretail-products-<version>.jar`
 5. Access endpoint using swagger-ui http://localhost:8080/swagger-ui/#/product-controller
 6. Access the mongodb client http://localhost:8081/db/myretail/
### Sample requests
 #### CURL
 1. `curl -X GET "http://localhost:8080/products/54456119" -H "accept: */*"`
 2. `curl -X PUT "http://localhost:8080/products/54456119" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"current_price\": { \"value\": 13.50 }}"`
 #### Swagger request json
 ```
 // for put endpoint
 { 
    "current_price": 
        { 
            "value": 13.50 
        }
 }
 ```
## Development notes
### Jacoco and PMD
Jacoco code coverage with PMD code analysis are performed as a part of maven build.
To skip pmd analysis, please use `-Dpmd.skip` parameter
The reports can be found in `/target/site` location

### Performance metrics
The application implements Micrometer JmxMeterRegistry for tracking performance.
This can be observed using locally available *Java Monitoring and Management Console*. 

1. execute `jconsole` from terminal to access the console. 
2. connect local process ends with the ProductApplication (insecure connection)
3. custom metrics are grouped under *metrics* folder