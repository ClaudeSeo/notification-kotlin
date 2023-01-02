package me.clud.notification.application.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

open class NotificationException(
  override val message: String,
  open val throwable: Throwable? = null,
  open val statusCode: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : RuntimeException(message, throwable)
