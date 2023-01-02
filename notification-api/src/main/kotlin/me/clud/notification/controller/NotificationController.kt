package me.clud.notification.controller

import jakarta.validation.Valid
import me.clud.notification.application.exception.NotificationException
import me.clud.notification.domain.notification.entity.Alimtalk
import me.clud.notification.domain.notification.entity.ServiceType
import me.clud.notification.domain.notification.port.inbound.SendNotificationUseCase
import me.clud.notification.domain.user.port.inbound.FindUserUseCase
import me.clud.notification.dto.alimtalk.SendAlimtalkRequestDto
import me.clud.notification.dto.alimtalk.SendAlimtalkResponseDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/notifications")
class NotificationController(
  val sendNotificationUseCase: SendNotificationUseCase,
  val findUserUseCase: FindUserUseCase
) {
  @PostMapping("/alimtalk")
  @ResponseStatus(code = HttpStatus.CREATED)
  fun sendAlimtalk(@RequestBody @Valid body: SendAlimtalkRequestDto): SendAlimtalkResponseDto {
    val user = findUserUseCase.findById(body.userId) ?: throw NotificationException(
      message = "유저 정보를 찾을 수 없습니다",
      statusCode = HttpStatus.NOT_FOUND
    )

    val alimtalk = Alimtalk(
      user = user,
      title = body.title,
      templateCode = body.templateCode,
      serviceType = ServiceType.valueOf(body.serviceType),
      senderGroupingKey = body.senderGroupingKey,
      recipientGroupingKey = body.recipientGroupingKey,
      isResend = body.isResend,
      reservationDate = body.reservationDateTime,
      templateParameter = body.templateParameter,
    )

    val messageUid = this.sendNotificationUseCase.execute(SendNotificationUseCase.Command(alimtalk))
    return SendAlimtalkResponseDto(messageUid)
  }
}
