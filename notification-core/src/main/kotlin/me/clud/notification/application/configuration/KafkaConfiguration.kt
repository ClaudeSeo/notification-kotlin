package me.clud.notification.application.configuration

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.record.CompressionType
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

@Configuration
class KafkaConfiguration {

  private fun createKafkaContainerFactory(brokers: String): KafkaTemplate<String, String> {
    val producerConfig = mapOf<String, Any>(
      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to brokers,
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
      ProducerConfig.ACKS_CONFIG to "1",
      ProducerConfig.COMPRESSION_TYPE_CONFIG to CompressionType.GZIP.name
    )

    return KafkaTemplate(DefaultKafkaProducerFactory(producerConfig))
  }

  @Primary
  @Bean
  fun kafkaClient(
    @Value("\${kafka.brokers.bootstrap}")
    brokers: String
  ): KafkaTemplate<String, String> {
    return createKafkaContainerFactory(brokers)
  }
}
