dependencies {
    // Spring
    api(libs.spring.boot.jpa)

    // Internal
    // implementation(project(":microservice-common"))

    // Database
//    implementation("org.postgresql:postgresql:$postgresqlVersion")
    api(libs.hibernate.validator)
    api(libs.hibernate.hikaricp)

    // Liquibase
    api(libs.liquibase.core)

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.powertest)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.h2)
//    testImplementation("org.testcontainers:junit-jupiter:$testContainerVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//    testImplementation("org.testcontainers:postgresql:$testContainerVersion")
}
