val kafkaAvroSerializerVersion: String by project

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/groups/public/")
    maven("https://packages.confluent.io/maven/")
}

dependencies {
    api("org.springframework.kafka:spring-kafka")
    api("io.confluent:kafka-avro-serializer:$kafkaAvroSerializerVersion")
}
