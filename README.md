# 🔒 Bakery Common Security

![Java](https://img.shields.io/badge/Java-21%2B-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)

Welcome to **Bakery Common Security**, the central authentication and JWT utility package of the Shah's Bakery Microservice Platform.

## 🎯 Purpose
This module provides framework-agnostic security utilities, specifically focusing on JSON Web Token (JWT) generation, validation, and parsing. It is strictly designed to be independent of Spring WebMVC or Spring WebFlux so it can be safely consumed by both the reactive API Gateway and blocking downstream services.

## 🛠️ Features
- **JWT Operations**: Centralized `JwtService` responsible for generating tokens (used by the Auth Service) and validating tokens (used by the API Gateway).
- **Security Constants**: Stores shared symmetric keys and authorization logic.
- **Framework Agnostic**: Intentionally omits web dependencies to prevent Tomcat/Netty classpath collisions.

## 📁 Folder Structure
```text
src/
└── main/
    └── java/org/blubakery/common/security/
        └── service/   # Framework-agnostic JWT generation and validation services.
```

## 🚀 Getting Started

### Local Setup
1. Include this library in your service's `build.gradle.kts`:
   ```kotlin
   implementation("org.blubakery.libs:bakery_common_security:1.0.0")
   ```

## 🔗 Related Links
- [Main Platform README](../README.md)
