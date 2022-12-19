plugins {
  kotlin(Plugin.kotlinSerialization) version Version.kotlin
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation(project(":notification-core"))
}
