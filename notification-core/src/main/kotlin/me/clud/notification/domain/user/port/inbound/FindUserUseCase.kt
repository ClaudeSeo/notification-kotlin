package me.clud.notification.domain.user.port.inbound

import me.clud.notification.domain.user.entity.User

interface FindUserUseCase {
  fun findById(userId: String): User?
}
