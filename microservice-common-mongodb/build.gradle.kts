dependencies {
    // Spring
    api(libs.spring.boot.mongodb)

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.powertest)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.embeded.mongo)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
