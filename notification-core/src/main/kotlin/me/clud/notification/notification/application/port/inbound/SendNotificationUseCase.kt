package me.clud.notification.notification.application.port.inbound

import me.clud.notification.notification.domain.Notification

interface SendNotificationUseCase {
  fun execute(command: Command): String

  data class Command(
    val notification: Notification
  )
}
