import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.kotlinx(module: String) = "org.jetbrains.kotlinx:$module:${Version.kotlinx}"

fun DependencyHandlerScope.kotlinxSerialization() =
  "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlinxSerialization}"

fun DependencyHandlerScope.commonPool2() = "org.apache.commons:commons-pool2:${Version.commonPool2}"

fun DependencyHandlerScope.springBoot(module: String) = "org.springframework.boot:$module"

fun DependencyHandlerScope.springBootRetry(module: String) = "org.springframework.retry:$module"

fun DependencyHandlerScope.springKafka() = "org.springframework.kafka:spring-kafka"

fun DependencyHandlerScope.jackson () = "com.fasterxml.jackson.module:jackson-module-kotlin:${Version.jackson}"
