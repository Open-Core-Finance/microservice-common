import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URI
import java.nio.file.Files
import java.util.*

val javaSourceCompatibility: String by project

val applicationVersion: String by project
val applicationGroup: String by project

/**
 * Define build script dependencies.
 */
buildscript {
    val springBootPluginVersion = "4.0.1"
    val springDependenciesManagementVersion = "1.1.7"

    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:$springBootPluginVersion")
        classpath("io.spring.gradle:dependency-management-plugin:$springDependenciesManagementVersion")
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:latest.release")
        classpath("io.freefair.gradle:lombok-plugin:latest.release")
    }
}

plugins {
    val springBootPluginVersion = "4.0.1"
    val springDependenciesManagementVersion = "1.1.7"
    val lombokPluginVersion = "9.1.0"
    val sonarqubePluginVersion = "7.2.2.6593"
    val hibernatePluginVersion = "7.2.0.Final"

    id("org.springframework.boot") version springBootPluginVersion apply false
    id("io.spring.dependency-management") version springDependenciesManagementVersion apply false
    id("io.freefair.lombok") version lombokPluginVersion apply false
    id("org.sonarqube") version sonarqubePluginVersion apply false
    id("org.hibernate.orm") version hibernatePluginVersion apply false
}

val publishOutputFolder = "publish-output"
extra["publishOutputFolder"] = publishOutputFolder

// Apply credentials
apply(from = if (file("artifact-credential.gradle.kts").exists()) "artifact-credential.gradle.kts" else "artifact-credential-sample.gradle.kts")

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "jacoco")
    apply(plugin = "application")
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "org.sonarqube")
    if (project.name != "jasypt-tool") {
        apply(plugin = "maven-publish")
        apply(plugin = "signing")
    }
    apply(plugin = "org.hibernate.orm")
    apply(plugin = "io.spring.dependency-management")

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    group = applicationGroup
    version = applicationVersion

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaSourceCompatibility))
        }
    }

    if (project.name != "common-kafka") {
        repositories {
            mavenLocal()
            mavenCentral()
            maven("https://s01.oss.sonatype.org/content/groups/public/")
        }
    }

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        val springBootPluginVersion = "4.0.1"
        val springCloudDependenciesVersion = "2025.1.0"
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootPluginVersion")
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudDependenciesVersion")
        }
    }

    tasks.withType<Jar> {
        archiveClassifier.set("")
        exclude("*.der", "*.xml", "*.yaml", "db/**", "resources/**", "**/*Application.class")
    }

    tasks.matching { it.name == "bootJar" }.configureEach {
        this as org.springframework.boot.gradle.tasks.bundling.BootJar
        archiveClassifier.set("boot")
        isEnabled = true
    }

    tasks.named<JacocoReport>("jacocoTestReport").configure {
        dependsOn(tasks.named<Test>("test"))
        mustRunAfter(tasks.named<Test>("test"))

        reports {
            html.required.set(true)
            xml.required.set(true)
            csv.required.set(true)

            html.outputLocation.set(layout.buildDirectory.dir("jacoco/html"))
            xml.outputLocation.set(layout.buildDirectory.file("jacoco/jacoco-report.xml"))
            csv.outputLocation.set(layout.buildDirectory.file("jacoco/jacoco-report.csv"))
        }

        // Delay setting classDirectories until after project evaluation
//        project.afterEvaluate {
//            val excludedPatterns = listOf("**/ex/**", "**/pojo/**", "**/model/**", "**/dto/**", "**/config/**")
//            classDirectories.setFrom(
//                classDirectories.files.map { dir ->
//                    fileTree(dir) {
//                        exclude(excludedPatterns)
//                    }
//                }
//            )
//        }
    }

    tasks.named<ProcessResources>("processTestResources") {
        from("../share-resources/test-app-resources")
        include("**/*")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        finalizedBy("jacocoTestReport")

        extensions.configure<JacocoTaskExtension> {
            classDumpDir = layout.buildDirectory.dir("jacoco/classpathdumps").get().asFile
            setDestinationFile(layout.buildDirectory.file("jacoco/${name}.exec").get().asFile)
            isEnabled = true
            sessionId = UUID.randomUUID().toString()
            output = JacocoTaskExtension.Output.FILE
        }
    }

    tasks.withType<Javadoc> {
        exclude("**/InternalServiceConfig#InternalServiceConfig")
        (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    }

    val javadocJar by tasks.registering(Jar::class) {
        dependsOn(tasks.named("javadoc"))
        from(tasks.named<Javadoc>("javadoc").get().destinationDir!!)
        archiveClassifier.set("javadoc")
        description = "Creates a Javadoc Jar for ${project.name}"
    }

    val sourceSets = extensions.getByType<JavaPluginExtension>().sourceSets

    val sourcesJar by tasks.registering(Jar::class) {
        dependsOn(tasks.named("classes"))
        archiveClassifier.set("sources")
        from(sourceSets.getByName("main").allSource)
    }

    artifacts {
        add("archives", javadocJar)
        add("archives", sourcesJar)
    }

    if (plugins.hasPlugin("maven-publish")) {
        tasks.named("publish").configure {
            dependsOn(tasks.named("build"))
            mustRunAfter(tasks.named("build"))
        }
    }

    afterEvaluate {
        dependencies {
            if (project.name != "microservice-common") {
                add("api", project(":microservice-common"))
            }
        }

        if (plugins.hasPlugin("maven-publish")) {
            configure<PublishingExtension> {
                repositories {
                    // Uncomment and configure if you need OSSRH upload
//                    maven {
//                        name = "ossrh-staging-api"
//                        url = uri(
//                            if (version.toString().endsWith("SNAPSHOT")) {
//                                "https://s01.oss.sonatype.org/content/repositories/snapshots"
//                            } else {
//                                "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/"
//                            }
//                        )
//                        credentials {
//                            username = rootProject.extra["artifactUsername"] as String
//                            password = rootProject.extra["artifactPassword"] as String
//                        }
//                    }
                    maven {
                        name = "maven-central"
                        url = layout.buildDirectory.dir(publishOutputFolder).get().asFile.toURI()
                    }
                }
                publications {
                    create<MavenPublication>("mavenJava") {
                        groupId = project.group.toString()
                        version = project.version.toString()

                        from(components["java"])
                        artifact(tasks.named("sourcesJar"))
                        artifact(tasks.named("javadocJar"))

                        pom {
                            name.set(project.name)
                            description.set("${project.name} library.")
                            url.set("https://corefinance.tech/")
                            properties.put("lib.name", project.name)

                            licenses {
                                license {
                                    name.set("The Apache License, Version 2.0")
                                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                                }
                            }
                            developers {
                                developer {
                                    id.set("dbaotrung")
                                    name.set("Trung Doan")
                                    email.set("doanbaotrung@gmail.com")
                                }
                            }
                            scm {
                                connection.set("scm:git:ssh://git@github.com:Open-Core-Finance/corefinance.git")
                                developerConnection.set("scm:git:ssh://git@github.com:Open-Core-Finance/corefinance.git")
                                url.set("https://github.com/Open-Core-Finance/corefinance")
                            }
                        }
                    }
                }
            }
        }
        if (plugins.hasPlugin("signing") && plugins.hasPlugin("maven-publish")) {
            val keyId = rootProject.extra["signing.keyId"] as? String
            val password = rootProject.extra["signing.password"] as? String
            val secretKeyRingFile = rootProject.extra["signing.secretKeyRingFile"] as? String
            extensions.configure<SigningExtension>("signing") {
//                if (secretKeyRingFile != null) {
//                    useGpgCmd() // Optional: use system gpg if preferred
//                    // OR use built-in secretKeyRingFile loader
//                    this.secretKeyRingFile = file(secretKeyRingFile)
//                }
//
//                this.password = password
//                this.signingKeyId = keyId
                sign(extensions.getByType<PublishingExtension>().publications)
            }
        }
    }

    tasks.register<Zip>("zipPublishFiles") {
        dependsOn("publish")
        mustRunAfter("publish")

        from(layout.buildDirectory.dir(publishOutputFolder))
        archiveFileName.set("${project.name}.zip")
        destinationDirectory.set(layout.buildDirectory.get().asFile)

        doLast {
            println("Published done!")
        }
    }

    tasks.register("uploadZip") {
        dependsOn("zipPublishFiles")
        mustRunAfter("zipPublishFiles")

        doLast {
            val zipFile = file("${layout.buildDirectory.get().asFile}/${project.name}.zip")
            val uploadUrl = "https://central.sonatype.com/api/v1/publisher/upload"
            val publishingType = "PUBLISHING"
            val projectName = project.name

            val username = rootProject.extra["artifactUsername"].toString()
            val password = rootProject.extra["artifactPassword"].toString()

            if (!zipFile.exists()) throw GradleException("Zip file not found: $zipFile")

            val encoded = Base64.getEncoder().encodeToString("$username:$password".toByteArray())
            val boundary = "===" + System.currentTimeMillis() + "==="
            val lineFeed = "\r\n"

            val connection = URI(uploadUrl).toURL().openConnection() as HttpURLConnection
            connection.doOutput = true
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $encoded")
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

            val outputStream = connection.outputStream
            val writer = OutputStreamWriter(outputStream, Charsets.UTF_8)

            fun writeFormField(name: String, value: String) {
                writer.write("--$boundary$lineFeed")
                writer.write("Content-Disposition: form-data; name=\"$name\"$lineFeed")
                writer.write("Content-Type: text/plain; charset=UTF-8$lineFeed")
                writer.write(lineFeed)
                writer.write("$value$lineFeed")
                writer.flush()
            }

            writeFormField("publishingType", publishingType)
            writeFormField("name", projectName)

            writer.write("--$boundary$lineFeed")
            writer.write("Content-Disposition: form-data; name=\"bundle\"; filename=\"${zipFile.name}\"$lineFeed")
            writer.write("Content-Type: application/zip$lineFeed")
            writer.write(lineFeed)
            writer.flush()

            Files.copy(zipFile.toPath(), outputStream)
            outputStream.flush()
            writer.write(lineFeed)
            writer.write("--$boundary--$lineFeed")
            writer.flush()
            writer.close()

            val responseCode = connection.responseCode
            val response = try {
                connection.inputStream.bufferedReader().readText()
            } catch (_: Exception) {
                connection.errorStream?.bufferedReader()?.readText() ?: "No error message"
            }

            println("Response ($responseCode): $response")

            if (responseCode >= 400) throw GradleException("Upload failed with response code $responseCode")
        }
    }
}

tasks.register("clean") {
    group = "build"
    description = "Cleans all subprojects"
    dependsOn(subprojects.map { it.tasks.named("clean") })
    mustRunAfter(subprojects.map { it.tasks.named("clean") })

    doLast {
        val buildDir = layout.buildDirectory.get().asFile
        if (buildDir.exists()) {
            println("Deleting root build directory: $buildDir")
            buildDir.deleteRecursively()
        }
    }
}

tasks.register<Copy>("copyDeployFilesFromChild") {
    dependsOn(subprojects.filter { it.tasks.findByName("publish") != null }.map { it.tasks.named("publish") })
    mustRunAfter(subprojects.filter { it.tasks.findByName("publish") != null }.map { it.tasks.named("publish") })

    subprojects.forEach { child ->
        from(child.layout.buildDirectory.dir(publishOutputFolder))
    }
    into(layout.buildDirectory.dir(publishOutputFolder))
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    doLast {
        println("copyDeployFilesFromChild done!")
    }
}

tasks.register<Zip>("zipPublishFilesFromChilds") {
    dependsOn("copyDeployFilesFromChild")
    mustRunAfter("copyDeployFilesFromChild")
    from(layout.buildDirectory.dir(publishOutputFolder))
    archiveFileName.set("${project.name}.zip")
    destinationDirectory.set(layout.buildDirectory.get().asFile)
    doLast {
        println("zipPublishFiles done!")
    }
}
