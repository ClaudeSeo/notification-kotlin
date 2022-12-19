import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  kotlin(Plugin.kotlinSerialization) version Version.kotlin
}

dependencies {
  implementation(springBoot("spring-boot-starter-data-redis"))
  implementation(springBoot("spring-boot-starter-webflux"))
  implementation(springBoot("spring-boot-starter-aop"))
  implementation(springBoot("spring-boot-starter-data-mongodb"))
  implementation(springBootRetry("spring-retry"))
}

tasks.getByName<BootJar>("bootJar") {
  enabled = false
}

tasks.getByName<Jar>("jar") {
  enabled = true
  archiveFileName.set("notification-core.jar")
}
