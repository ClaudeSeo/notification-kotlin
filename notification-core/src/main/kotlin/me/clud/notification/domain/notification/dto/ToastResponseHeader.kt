package me.clud.notification.domain.notification.dto

import kotlinx.serialization.Serializable

@Serializable
data class ToastResponseHeader(
  val resultCode: Int,
  val resultMessage: String,
  val isSuccessful: Boolean
)
