val embededMongoVersion: String by project
val powerMockitoVersion: String by project
val junitVersion: String by project

dependencies {
    // Spring
    api("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.powermock:powermock-api-mockito2:$powerMockitoVersion")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:$embededMongoVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

//publishing {
//    repositories {
//        // Uncomment and configure if you need OSSRH upload
//        /*
//        maven {
//            name = "ossrh-staging-api"
//            url = uri(
//                if (version.toString().endsWith("SNAPSHOT")) {
//                    "https://s01.oss.sonatype.org/content/repositories/snapshots"
//                } else {
//                    "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
//                }
//            )
//            credentials {
//                username = rootProject.extra["artifactUsername"] as String
//                password = rootProject.extra["artifactPassword"] as String
//            }
//        }
//        */
//        maven {
//            name = "maven-central"
//            url = layout.buildDirectory.dir(rootProject.extra["publishOutputFolder"] as String).get().asFile.toURI()
//        }
//    }
//
//    publications {
//        create<MavenPublication>("mavenJava") {
//            groupId = project.group.toString()
//            version = project.version.toString()
//
//            from(components["java"])
//            artifact(tasks.named("sourcesJar"))
//            artifact(tasks.named("javadocJar"))
//
//            pom {
//                name.set(project.name)
//                description.set("${project.name} library.")
//                url.set("https://corefinance.tech/")
//                properties.put("lib.name", project.name)
//
//                licenses {
//                    license {
//                        name.set("The Apache License, Version 2.0")
//                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                    }
//                }
//                developers {
//                    developer {
//                        id.set("dbaotrung")
//                        name.set("Trung Doan")
//                        email.set("doanbaotrung@gmail.com")
//                    }
//                }
//                scm {
//                    connection.set("scm:git:ssh://git@github.com:Open-Core-Finance/corefinance.git")
//                    developerConnection.set("scm:git:ssh://git@github.com:Open-Core-Finance/corefinance.git")
//                    url.set("https://github.com/Open-Core-Finance/corefinance")
//                }
//            }
//        }
//    }
//}
