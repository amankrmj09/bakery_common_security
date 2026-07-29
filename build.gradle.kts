plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("io.spring.dependency-management") version "1.1.7"
}

description = "Shared DTOs and Common Utilities for Bakery Microservices"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "GitHubBakeryCommonCore"
        url = uri("https://maven.pkg.github.com/amankrmj09/bakery_common_core")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

extra["springCloudVersion"] = "2025.0.3"

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.15")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    // 2. Spring Boot Core & Web
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.blubakery.libs:bakery_common_core:1.0.1")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    
    // 6. Security
    api("org.springframework.boot:spring-boot-starter-security")
    
    // 8. Tooling & Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("Bakery Common Security")
                description.set("Shared DTOs and Common Utilities for Bakery Microservices")
                url.set("https://github.com/amankrmj09/bakery_common_security")
                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("amankrmj09")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/amankrmj09/bakery_common_security")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks.withType<GenerateModuleMetadata> {
    suppressedValidationErrors.add("dependencies-without-versions")
}
