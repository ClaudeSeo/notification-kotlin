package me.clud.notification.notification.domain.toast

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
enum class AlimtalkResendType {
  SMS, LMS
}

@Serializable
enum class ServiceType(val value: String) {
  API("api")
}

@Serializable
data class ToastAlimtalkResendParameter(
  val isResend: Boolean,
  val resendType: AlimtalkResendType? = null,
  val resendTitle: String? = null,
  val resendContent: String? = null,
  val resendSendNo: String? = null
)

@Serializable
data class ToastAlimtalkRecipient(
  val recipientNo: String,
  val templateParameter: Map<String, String>? = null,
  val recipientGroupingKey: String? = null,
  val resendParameter: ToastAlimtalkResendParameter? = null
)

@Serializable
data class ToastAlimtalkBody(
  val plusFriendId: String,
  val templateCode: String,
  val recipientList: List<ToastAlimtalkRecipient>,
  val requestDate: String? = null,
  val senderGroupingKey: String? = null,
)

fun ToastAlimtalkBody.toJSON() = Json.encodeToString(this)
