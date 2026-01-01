// Define versions at the top (adjust as needed, or replace by project property delegation)
val springDocApiVersion: String by project
val beanutilsVersion: String by project
val jasyptVersion: String by project
val javaJwtVersion: String by project
val powerMockitoVersion: String by project
val junitVersion: String by project
val springBootStartedAopVersion: String by project

dependencies {
    // Spring
    api("org.springframework.boot:spring-boot-starter-tomcat")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-aop:$springBootStartedAopVersion")
    api("org.springframework.data:spring-data-commons")
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-json")
    api("org.springframework.boot:spring-boot-starter-jackson")

    // Spring doc
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocApiVersion")

    // Common
    api("commons-beanutils:commons-beanutils:$beanutilsVersion")

    // Security
    api("com.auth0:java-jwt:$javaJwtVersion")

    // Compile only
    compileOnly("org.springframework.boot:spring-boot-starter-data-mongodb") {
        // Exclude because current version of snakeyaml has vulnerabilities. Need re-define.
        exclude(group = "org.springframework.boot", module = "spring-boot-starter")
    }

    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("org.hibernate.orm:hibernate-core")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter")
    }
    testImplementation("org.powermock:powermock-api-mockito2:$powerMockitoVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
