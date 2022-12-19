package me.clud.notification.notification.adapter

import me.clud.notification.exception.NotificationException
import me.clud.notification.logger
import me.clud.notification.notification.application.port.outbound.NotificationPort
import me.clud.notification.notification.domain.Alimtalk
import me.clud.notification.notification.domain.toast.ToastAlimtalkSendResponse
import me.clud.notification.notification.domain.toast.toJSON
import me.clud.notification.notification.domain.toToastAlimtalkBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class NotificationAdapter(
  private val webClient: WebClient,

  @Value("\${toast.alimtalk.url")
  private val alimtalkUrl: String,

  @Value("\${toast.alimtalk.appKey}")
  private val alimtalkAppKey: String,

  @Value("\${toast.alimtalk.secretKey}")
  private val alimtalkSecretKey: String
) : NotificationPort {
  private val logger = logger()

  private val alimtalkClient = webClient.mutate()
    .baseUrl("https://f63a7387-2ff7-47f4-b081-33cfa945fed7.mock.pstmn.io")
    .defaultHeader("X-Secret-Key", alimtalkSecretKey)
    .build()

  private fun isSuccessSend(response: ToastAlimtalkSendResponse) {
    if (!response.header.isSuccessful) {
      throw NotificationException(message = response.header.resultMessage, statusCode = HttpStatus.BAD_REQUEST)
    }

    if (response.message == null) {
      throw NotificationException(message = "잘못된 결과 값입니다.", statusCode = HttpStatus.INTERNAL_SERVER_ERROR)
    }

    response.message.sendResults.forEach { it ->
      if (it.resultCode != 0) throw NotificationException(
        message = it.resultMessage,
        statusCode = HttpStatus.BAD_REQUEST
      )
    }
  }

  override fun sendAlimtalk(alimtalk: Alimtalk): String {
    val url = "/alimtalk/v1.4/appkeys/$alimtalkAppKey/messages"
    val result = try {
      runBlocking(Dispatchers.IO) {
        alimtalkClient.post()
          .uri(url)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(alimtalk.toToastAlimtalkBody().toJSON())
          .retrieve()
          .awaitBody<ToastAlimtalkSendResponse>()
          .also {
            isSuccessSend(it)
          }
      }
    } catch (e: NotificationException) {
      throw e
    } catch (e: Exception) {
      throw NotificationException(message = "발송 처리 중 에러가 발생했습니다.", statusCode = HttpStatus.INTERNAL_SERVER_ERROR)
    }

    return result.message?.requestId ?: throw NotificationException(
      "발송 처리 중 에러가 발생했습니다.",
      statusCode = HttpStatus.INTERNAL_SERVER_ERROR
    )
  }
}
