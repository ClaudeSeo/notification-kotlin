package me.clud.notification.notification.domain

import me.clud.notification.user.domain.User

interface Notification {
  val user: User
}
