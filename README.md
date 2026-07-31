# Bakery Common Security Module

This module is a shared Spring Security library used across the various microservices in the Blu's Bakery ecosystem. It centralizes common security configurations, JWT validation logic, and authorization primitives, ensuring a consistent and secure approach to API access control across all services.

## Purpose

The primary purpose of this module is to abstract away the boilerplate code required to secure Spring Boot applications. It provides:
- **Shared Spring Security Configuration**: Pre-configured `SecurityFilterChain` that sets up stateless session management, CORS rules, and disables CSRF (typical for stateless REST APIs).
- **JWT Validation Logic**: Utility classes to parse JSON Web Tokens (JWTs), verify their signatures against a shared secret or public key, and extract claims (such as the user's ID and role).
- **JwtAuthenticationFilter**: A custom `OncePerRequestFilter` that intercepts incoming HTTP requests, extracts the JWT from the `Authorization` header, validates it, and sets the authenticated user's details in the Spring Security `SecurityContext`.
- **Role Enum Definitions**: A centralized enum defining the various roles within the system (e.g., `CUSTOMER`, `ADMIN`, `STORE_MANAGER`), ensuring consistent role-based access control (RBAC) terminology across all microservices.

## Folder Structure

A typical folder structure for this shared module looks like this:

```
bakery_common_security/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/blusbakery/common/security/
│   │   │       ├── config/
│   │   │       │   └── SecurityConfig.java         # Base Spring Security configuration
│   │   │       ├── filter/
│   │   │       │   └── JwtAuthenticationFilter.java  # Intercepts requests to validate JWTs
│   │   │       ├── jwt/
│   │   │       │   └── JwtUtil.java                # Logic for parsing and validating JWT signatures
│   │   │       └── model/
│   │   │           └── Role.java                   # Centralized Role enum (CUSTOMER, ADMIN, etc.)
│   │   └── resources/
│   └── test/
└── pom.xml / build.gradle                        # Dependency management for the shared library
```

## How Microservices Integrate This Module

To secure its REST endpoints, a microservice integrates this shared module by following these steps:

1. **Add the Dependency**: Include `bakery_common_security` as a dependency in the microservice's `pom.xml` or `build.gradle`.
2. **Import Configuration**: Import the shared security configuration class into the microservice's context. This is often done using `@Import(SecurityConfig.class)` on the main application class or a local configuration class.
3. **Component Scanning**: Ensure that the packages from this module (e.g., `com.blusbakery.common.security`) are included in the Spring component scan if necessary.
4. **Endpoint Security**: With the filter and configuration in place, the microservice can use standard Spring Security annotations like `@PreAuthorize` to secure its endpoints based on roles.

**Example Integration:**

```java
@SpringBootApplication
@Import(SecurityConfig.class)
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

**Example Endpoint Security:**

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<Order> createOrder(...) {
        // Only accessible by users with the CUSTOMER role
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STORE_MANAGER')")
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(...) {
        // Only accessible by ADMINs or STORE_MANAGERs
    }
}
```

## JWT Validation Process

It is important to note that **this module does NOT generate JWTs**. Token generation is strictly handled by the centralized Authentication/Identity Service (e.g., `bakery_auth_service`) upon successful user login.

This shared module is solely responsible for **validation**:

1. **Extraction**: The `JwtAuthenticationFilter` extracts the token from the `Authorization: Bearer <token>` header of incoming requests.
2. **Signature Verification**: Using the `JwtUtil`, the module verifies the cryptographic signature of the token to ensure it has not been tampered with. This typically involves a shared secret key or an asymmetric public key.
3. **Expiration Check**: The module checks the `exp` (expiration time) claim to ensure the token is still valid.
4. **Claim Extraction**: Upon successful verification, claims such as the username and roles are extracted.
5. **Context Setup**: A Spring Security `UsernamePasswordAuthenticationToken` is created and placed in the `SecurityContextHolder`, allowing the rest of the application to know *who* is making the request and *what* their authorities are.
