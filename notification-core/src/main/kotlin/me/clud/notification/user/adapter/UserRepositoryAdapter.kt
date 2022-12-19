package me.clud.notification.user.adapter

import me.clud.notification.logger
import me.clud.notification.user.application.port.outbound.UserRepositoryPort
import me.clud.notification.user.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service()
class UserRepositoryAdapter(private val webClient: WebClient) : UserRepositoryPort {
  private val logger = logger()

  @Retryable(include = [Exception::class], maxAttempts = 2, backoff = Backoff(1000))
  override fun findOne(userId: String): User {
    val result = runBlocking(Dispatchers.IO) {
      logger.info("데이터 조회: $userId")
      webClient.mutate().baseUrl("https://naver.com").build().get().retrieve().awaitBody<String>()
    }

    logger.info("유저 정보 조회 : $result")

    return User(
      id = userId,
      email = "test@gmail.com",
      phoneNumber = "010-0000-0000"
    )
  }
}
