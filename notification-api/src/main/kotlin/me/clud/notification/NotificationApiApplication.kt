package me.clud.notification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Import

@SpringBootApplication
@EnableCaching
@Import(DomainConfigureLoader::class)
class NotificationApiApplication

fun main(args: Array<String>) {
  runApplication<NotificationApiApplication>(*args)
}
