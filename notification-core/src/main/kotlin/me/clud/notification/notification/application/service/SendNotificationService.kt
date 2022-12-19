package me.clud.notification.notification.application.service

import me.clud.notification.logger
import me.clud.notification.notification.application.port.inbound.SendNotificationUseCase
import me.clud.notification.notification.application.port.outbound.NotificationPort
import me.clud.notification.notification.domain.Alimtalk
import org.springframework.stereotype.Service

@Service
class SendNotificationService(private val notificationPort: NotificationPort) : SendNotificationUseCase {
  private val logger = logger()

  override fun execute(command: SendNotificationUseCase.Command): String {
    logger.info("알림톡 발송 요청: $command")
    when (command.notification) {
      is Alimtalk -> return notificationPort.sendAlimtalk(command.notification)
      else -> throw IllegalArgumentException("잘못된 요청입니다.")
    }
  }
}
