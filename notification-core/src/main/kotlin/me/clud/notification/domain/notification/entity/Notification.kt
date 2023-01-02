package me.clud.notification.domain.notification.entity

import me.clud.notification.domain.user.entity.User

interface Notification {
  val user: User
}
