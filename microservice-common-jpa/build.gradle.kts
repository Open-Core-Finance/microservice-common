val powerMockitoVersion: String by project
val junitVersion: String by project

val hibernateVersion: String by project
val hibernateValidatorVersion: String by project
val liquibaseVersion: String by project
val h2Version: String by project

//val postgresqlVersion: String by project
//val testContainerVersion: String by project

dependencies {
    // Spring
    api("org.springframework.boot:spring-boot-starter-data-jpa")

    // Internal
    // implementation(project(":microservice-common"))

    // Database
//    implementation("org.postgresql:postgresql:$postgresqlVersion")
    api("org.hibernate.validator:hibernate-validator:$hibernateValidatorVersion")
    api("org.hibernate.orm:hibernate-hikaricp:$hibernateVersion")

    // Liquibase
    api("org.liquibase:liquibase-core:$liquibaseVersion")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.powermock:powermock-api-mockito2:$powerMockitoVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
//    testImplementation("org.testcontainers:junit-jupiter:$testContainerVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//    testImplementation("org.testcontainers:postgresql:$testContainerVersion")
    testImplementation("com.h2database:h2:${h2Version}")
}
