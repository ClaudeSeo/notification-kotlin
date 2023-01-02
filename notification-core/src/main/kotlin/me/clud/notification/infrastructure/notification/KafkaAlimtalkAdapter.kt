package me.clud.notification.infrastructure.notification

import kotlinx.coroutines.runBlocking
import me.clud.notification.await
import me.clud.notification.domain.notification.dto.toJSON
import me.clud.notification.domain.notification.entity.Alimtalk
import me.clud.notification.domain.notification.port.outbound.AlimtalkPort
import me.clud.notification.infrastructure.KafkaTopic
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class KafkaAlimtalkAdapter(private val kafkaTemplate: KafkaTemplate<String, String>) : AlimtalkPort {
  override fun sendAlimtalk(alimtalk: Alimtalk): String {
    runBlocking {
      kafkaTemplate.send(KafkaTopic.NOTIFICATION_SEND_QUEUE, alimtalk.toDTO().toJSON()).await()
    }

    return UUID.randomUUID().toString()
  }
}
