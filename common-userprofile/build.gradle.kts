// Version properties (these should be defined in gradle.properties or passed in via project)
val powerMockitoVersion: String by project
val h2Version: String by project
val junitVersion: String by project

dependencies {
    // Internal
    api(project(":microservice-common-jpa"))

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.powermock:powermock-api-mockito2:$powerMockitoVersion")
    testImplementation("com.h2database:h2:$h2Version")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
