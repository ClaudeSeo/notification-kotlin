package me.clud.notification.domain.notification.port.outbound

import me.clud.notification.domain.notification.entity.Alimtalk

interface AlimtalkPort {
  fun sendAlimtalk(alimtalk: Alimtalk): String
}
