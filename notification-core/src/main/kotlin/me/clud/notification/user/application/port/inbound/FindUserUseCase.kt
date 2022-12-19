package me.clud.notification.user.application.port.inbound

import me.clud.notification.user.domain.User

interface FindUserUseCase {
  fun findById(userId: String): User?
}
