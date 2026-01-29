dependencies {

    // Spring
//    api(libs.spring.boot.tomcat)
//    api(libs.spring.boot.validation)
//    api(libs.spring.boot.web)
//    api(libs.spring.boot.json)
//    api(libs.spring.boot.jackson)
    api(libs.bundles.spring.boot.starter)

    api(libs.spring.boot.aop)
    api(libs.spring.data.commons)
    api(libs.spring.boot.security)


    // Spring doc
    api(libs.spring.doc.api)

    // Common
    api(libs.beanutils)

    // Security
    api(libs.java.jwt)

    // Compile only
    compileOnly(libs.spring.boot.mongodb)

    implementation(libs.persistence.api)
    implementation(libs.hibernate.core)

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.powertest)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
