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

*For overall architecture, contribution guidelines, and security policies, please refer to the main [Blu's Bakery](https://github.com/amankrmj09/Blu_s_Bakery) repository.*

