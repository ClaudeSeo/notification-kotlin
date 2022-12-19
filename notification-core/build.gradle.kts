import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  kotlin(Plugin.kotlinSerialization) version Version.kotlin
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-aop")
  implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
  implementation("org.springframework.retry:spring-retry")
}

tasks.getByName<BootJar>("bootJar") {
  enabled = false
}

tasks.getByName<Jar>("jar") {
  enabled = true
}
