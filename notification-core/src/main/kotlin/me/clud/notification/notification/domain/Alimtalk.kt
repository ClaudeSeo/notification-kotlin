package me.clud.notification.notification.domain

import me.clud.notification.notification.domain.toast.ServiceType
import me.clud.notification.notification.domain.toast.ToastAlimtalkBody
import me.clud.notification.notification.domain.toast.ToastAlimtalkRecipient
import me.clud.notification.notification.domain.toast.ToastAlimtalkResendParameter
import me.clud.notification.toKST
import me.clud.notification.user.domain.User
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

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
) : Notification

fun Alimtalk.toToastAlimtalkBody() = ToastAlimtalkBody(
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
