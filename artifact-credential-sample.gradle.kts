// Define extra properties in Kotlin using the `extra` delegate
extra["artifactUsername"] = "your-jira-id"
extra["artifactPassword"] = "your-jira-password"
extra["artifactSnapshotUrl"] = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
extra["artifactReleaseUrl"] = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
//extra["JRELEASER_GPG_SECRET_KEY"] = "<KEY>"
//extra["JRELEASER_GPG_PUBLIC_KEY"] = "<KEY>"

// For keys with dots, use quoted strings as keys in extra map
extra["signing.keyId"] = "0xDB1F5ADC"
extra["signing.password"] = "password"
extra["signing.secretKeyRingFile"] = "../secring.gpg"