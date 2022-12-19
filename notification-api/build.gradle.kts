import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  kotlin(Plugin.kotlinSerialization) version Version.kotlin
}

dependencies {
  implementation(springBoot("spring-boot-starter-validation"))
  implementation(project(":notification-core"))
}

tasks.getByName<BootJar>("bootJar") {
  enabled = true
  archiveFileName.set("notification-api.jar")
}

tasks.getByName<Jar>("jar") {
  enabled = false
}
