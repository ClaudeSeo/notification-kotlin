package me.clud.notification.notification.domain.toast

import kotlinx.serialization.Serializable

@Serializable
data class ToastResponseHeader(
  val resultCode: Int,
  val resultMessage: String,
  val isSuccessful: Boolean
)
