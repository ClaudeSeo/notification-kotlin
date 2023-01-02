package me.clud.notification.domain.notification.entity

import kotlinx.serialization.Serializable
import me.clud.notification.domain.notification.dto.ToastAlimtalkBody
import me.clud.notification.domain.notification.dto.ToastAlimtalkRecipient
import me.clud.notification.domain.notification.dto.ToastAlimtalkResendParameter
import me.clud.notification.domain.user.entity.User
import me.clud.notification.toKST
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Serializable
enum class ServiceType(val value: String) {
  API("api")
}

data class Alimtalk(
  override val user: User,
  val serviceType: ServiceType,
  val templateCode: String,
  val title: String?,
  val reservationDate: OffsetDateTime? = null,
  val templateParameter: Map<String, String> = mapOf(),
  val senderGroupingKey: String? = null,
  val recipientGroupingKey: String? = null,
  val isResend: Boolean? = false
) : Notification {
  fun toDTO() = ToastAlimtalkBody(
    plusFriendId = serviceType.value,
    templateCode = templateCode,
    requestDate = reservationDate?.let { it.toKST().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) },
    senderGroupingKey = senderGroupingKey,
    recipientList = listOf(
      ToastAlimtalkRecipient(
        recipientNo = user.phoneNumber,
        templateParameter = templateParameter,
        recipientGroupingKey = recipientGroupingKey,
        resendParameter = ToastAlimtalkResendParameter(
          isResend = isResend ?: false,
        )
      )
    )
  )
}
