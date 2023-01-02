package me.clud.notification.domain.notification.service

import me.clud.notification.domain.notification.entity.Alimtalk
import me.clud.notification.domain.notification.port.inbound.SendNotificationUseCase
import me.clud.notification.domain.notification.port.outbound.AlimtalkPort
import me.clud.notification.logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class SendNotificationService(@Qualifier("kafkaAlimtalkAdapter") private val alimtalkPort: AlimtalkPort) :
  SendNotificationUseCase {
  private val logger = logger()

  override fun execute(command: SendNotificationUseCase.Command): String {
    logger.info("알림톡 발송 요청: $command")
    when (command.notification) {
      is Alimtalk -> return alimtalkPort.sendAlimtalk(command.notification)
      else -> throw IllegalArgumentException("잘못된 요청입니다.")
    }
  }
}
