package me.clud.notification.domain.notification.dto

import kotlinx.serialization.Serializable

@Serializable
data class ToastAlimtalkSendResponseSender(
  val recipientSeq: Int,
  val recipientNo: String,
  val resultCode: Int,
  val resultMessage: String,
  val recipientGroupingKey: String
)

@Serializable
data class ToastAlimtalkSendResponseMessage(
  val requestId: String,
  val senderGroupingKey: String,
  val sendResults: List<ToastAlimtalkSendResponseSender>
)

@Serializable
data class ToastAlimtalkSendResponse(
  val header: ToastResponseHeader,
  val message: ToastAlimtalkSendResponseMessage?
)
