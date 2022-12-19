package me.clud.notification.dto.alimtalk

import me.clud.notification.annotation.ValueOfEnum
import me.clud.notification.notification.domain.toast.ServiceType
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotBlank
import java.time.OffsetDateTime

data class SendAlimtalkRequestDto(
  @field:NotBlank(message = "서비스 타입은 필수입니다.")
  @field:ValueOfEnum(enumClass = ServiceType::class, message = "지원하지 않는 서비스 타입입니다.")
  val serviceType: String = "",

  @field:NotBlank(message = "유저 ID 는 필수입니다.")
  val userId: String = "",

  @field:NotBlank(message = "템플릿 코드는 필수입니다.")
  val templateCode: String = "",

  val senderGroupingKey: String?,

  val recipientGroupingKey: String?,

  val title: String?,

  @field:Future
  val reservationDateTime: OffsetDateTime?,

  val templateParameter: Map<String, String> = mapOf(),

  val isResend: Boolean = false,
)
