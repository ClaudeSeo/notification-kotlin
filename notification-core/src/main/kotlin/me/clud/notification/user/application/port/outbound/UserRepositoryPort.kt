package me.clud.notification.user.application.port.outbound

import me.clud.notification.user.domain.User

interface UserRepositoryPort {
  fun findOne(userId: String): User?
}
