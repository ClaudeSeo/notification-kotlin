package me.clud.notification.domain.user.service

import me.clud.notification.domain.user.entity.User
import me.clud.notification.domain.user.port.inbound.FindUserUseCase
import me.clud.notification.domain.user.port.outbound.UserRepositoryPort
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class FindUserService(private val userRepositoryPort: UserRepositoryPort) : FindUserUseCase {

  @Cacheable("user", key = "#userId", unless = "#result==null")
  override fun findById(userId: String): User? {
    return userRepositoryPort.findOne(userId)
  }
}
