# UserManagement

This is a RESTful API for managing user registration and login, built with Spring Boot, Spring Data JPA, Lombok, JWT authentification and Swagger.

## Prerequisites

* Java 23 
* Maven
* jwt security

## How to Run

1.  Clone the repository: `git clone <repository-url>`
2.  Navigate to the project directory: `cd userManagement`
3.  Build the project: `mvn clean install` or simply go to the maven setting in your IDE
4.  Run the application: `mvn spring-boot:run`
5.  Alternatively, you can just open the project in your intelliJ IDE and run the application main file that is : `UserManagementApplication.java`
6.  open PostMan to Test the API

## Swagger Documentation

Access the API documentation at: `http://localhost:8080/swagger-ui/index.html`

## Example Requests in PostMan

### Get All users with the Get request

```
url http://localhost:8080/users
```

### user registration with the POST request
```
url http://localhost:8080/users/register

{
    "id": "1",
    "name": "john",
    "email": "john@gmail.com"
    "password" : "132h2hj"
}
```

