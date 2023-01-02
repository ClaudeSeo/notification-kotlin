package me.clud.notification.domain.user.port.outbound

import me.clud.notification.domain.user.entity.User

interface UserRepositoryPort {
  fun findOne(userId: String): User?
}
