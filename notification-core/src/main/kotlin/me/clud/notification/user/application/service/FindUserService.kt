package me.clud.notification.user.application.service

import me.clud.notification.user.application.port.inbound.FindUserUseCase
import me.clud.notification.user.application.port.outbound.UserRepositoryPort
import me.clud.notification.user.domain.User
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class FindUserService(private val userRepositoryPort: UserRepositoryPort) : FindUserUseCase {

  @Cacheable("user", key = "#userId", unless = "#result==null")
  override fun findById(userId: String): User? {
    return userRepositoryPort.findOne(userId)
  }
}
