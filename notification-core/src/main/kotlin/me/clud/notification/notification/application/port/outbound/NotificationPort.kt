package me.clud.notification.notification.application.port.outbound

import me.clud.notification.notification.domain.Alimtalk

interface NotificationPort {
  fun sendAlimtalk(alimtalk: Alimtalk): String
}
