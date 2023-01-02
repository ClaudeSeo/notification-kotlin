package me.clud.notification.infrastructure.user

import me.clud.notification.domain.user.entity.User
import me.clud.notification.domain.user.port.outbound.UserRepositoryPort
import me.clud.notification.logger
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service

@Service()
class MongoUserRepository(private val mongoTemplate: MongoTemplate) : UserRepositoryPort {
  private val logger = logger()

  @Retryable(include = [Exception::class], maxAttempts = 2, backoff = Backoff(1000))
  override fun findOne(userId: String): User? {
    val query = Query().addCriteria(UserDataModel::id isEqualTo userId)
    val user = mongoTemplate.findOne(query, UserDataModel::class.java)
    return user?.toEntity()
  }
}
