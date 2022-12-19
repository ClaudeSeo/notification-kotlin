package me.clud.notification.configuration

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.WriteTimeoutHandler
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Configuration
@EnableRetry
class WebClientConfiguration {

  object KSerializationConfig {
    @ExperimentalSerializationApi
    val json = Json {
      ignoreUnknownKeys = true
      isLenient = true
      allowSpecialFloatingPointValues = true
      useArrayPolymorphism = true
      encodeDefaults = true
      explicitNulls = false
    }
  }

  @ExperimentalSerializationApi
  private fun createRequestFactory(
    connectionTimeout: Duration,
    readTimeout: Duration,
  ): WebClient {
    val exchangeStrategies = ExchangeStrategies.builder()
      .codecs { configurer ->
        val decoder = KotlinSerializationJsonDecoder(KSerializationConfig.json)
        val codecs = configurer.defaultCodecs()
        codecs.maxInMemorySize(1024 * 1024 * 2)
        codecs.kotlinSerializationJsonDecoder(decoder)
      }
      .build()

    val httpClient = HttpClient.create()
      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout.toMillis().toInt())
      .doOnConnected { conn -> conn.addHandlerLast(WriteTimeoutHandler(readTimeout.toSeconds().toInt())) }

    return WebClient.builder()
      .clientConnector(ReactorClientHttpConnector(httpClient))
      .exchangeStrategies(exchangeStrategies)
      .build()
  }

  @Bean
  @ExperimentalSerializationApi
  fun webClient(): WebClient {
    return createRequestFactory(
      connectionTimeout = Duration.ofMillis(500),
      readTimeout = Duration.ofSeconds(6)
    )
  }

  @Bean
  fun retryTemplate(): RetryTemplate {
    val retryTemplate = RetryTemplate()
    val fixedBackOffPolicy = FixedBackOffPolicy()
    fixedBackOffPolicy.backOffPeriod = 1000L

    val retryPolicy = SimpleRetryPolicy()
    retryPolicy.maxAttempts = 2

    retryTemplate.setRetryPolicy(retryPolicy)
    retryTemplate.setBackOffPolicy(fixedBackOffPolicy)
    return retryTemplate
  }
}
