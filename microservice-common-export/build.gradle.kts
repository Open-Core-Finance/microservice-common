val powerMockitoVersion: String by project
val junitVersion: String by project
val apachePoiVersion: String by project

dependencies {
    // Export libraries
    api("org.apache.poi:poi-ooxml:$apachePoiVersion")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.powermock:powermock-api-mockito2:$powerMockitoVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
