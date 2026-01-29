dependencies {
    // Internal
    api(project(":microservice-common-jpa"))

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.powertest)
    testImplementation(libs.h2)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
