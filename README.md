# Bakery Common Security Module

This module is a shared Spring Security library used across the microservices in the Blu's Bakery ecosystem. It centralizes common security configurations, header-based authentication filtering, and security exception primitives, ensuring a consistent and secure approach to method-level access control across all services.

## Purpose & Architecture

The primary purpose of this module is to eliminate redundant security boilerplate across microservices while enforcing uniform access control policies:

- **Shared Method Security Configuration (`MethodSecurityConfig`)**: A Spring `@Configuration` class that sets up a stateless `SecurityFilterChain` (`SessionCreationPolicy.STATELESS`), disables CSRF, handles 401 Unauthorized and 403 Forbidden responses, and enables method-level security via `@EnableMethodSecurity`.
- **Header Authentication Filter (`HeaderAuthenticationFilter`)**: A custom `OncePerRequestFilter` that inspects incoming HTTP headers (`X-User-Id` and `X-User-Role`), builds a Spring `UsernamePasswordAuthenticationToken` with granted authority `ROLE_<ROLE>`, and sets it into the `SecurityContextHolder`.
- **Security Exceptions**: Shared exception classes (`InvalidTokenException`, `UnauthenticatedException`, `UnauthorizedAccessException`) for handling security violation scenarios uniformly.

## Folder Structure

```
bakery_common_security/
├── .github/
│   └── workflows/
│       └── publish.yml
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   └── main/
│       ├── java/
│       │   └── org/blubakery/common/security/
│       │       ├── exception/
│       │       │   └── security/
│       │       │       ├── InvalidTokenException.java
│       │       │       ├── UnauthenticatedException.java
│       │       │       └── UnauthorizedAccessException.java
│       │       └── security/
│       │           ├── HeaderAuthenticationFilter.java
│       │           └── MethodSecurityConfig.java
│       └── resources/
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── README.md
```

## 🔗 Related Links

- [Parent Repository](https://github.com/amankrmj09/Blu_s_Bakery)

## How Microservices Integrate This Module

### 1. Gradle Dependency

Add `bakery_common_security` as a dependency in your microservice's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("org.blubakery.libs:bakery_common_security:2.0.0")
}
```

### 2. Import Security Configuration

Import `MethodSecurityConfig` in your main Spring Boot application class or configuration class:

```java
package org.blubakery.orderservice;

import org.blubakery.common.security.security.MethodSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MethodSecurityConfig.class)
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
```

### 3. Endpoint Authorization

Secure controllers and services using Spring Security method annotations:

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
```

## Request Authentication Pipeline

```
[Incoming Request from Gateway]
         │ (Headers: X-User-Id, X-User-Role)
         ▼
[HeaderAuthenticationFilter]
         │ 1. Extract X-User-Id & X-User-Role
         │ 2. Construct GrantedAuthority ("ROLE_" + role)
         │ 3. Populate SecurityContextHolder
         ▼
[MethodSecurityConfig Filter Chain]
         │ (Session: STATELESS, CSRF: Disabled)
         ▼
[Spring Security Method Interceptor (@PreAuthorize)]
         │ 
         ├── Granted ──► Controller Action Executed
         └── Denied  ──► 403 Forbidden Response / Exception
```
