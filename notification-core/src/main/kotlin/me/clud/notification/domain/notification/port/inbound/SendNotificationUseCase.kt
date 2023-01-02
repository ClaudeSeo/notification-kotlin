package me.clud.notification.domain.notification.port.inbound

import me.clud.notification.domain.notification.entity.Notification

interface SendNotificationUseCase {
  fun execute(command: Command): String

  data class Command(
    val notification: Notification
  )
}
