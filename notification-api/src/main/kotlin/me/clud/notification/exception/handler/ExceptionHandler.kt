package me.clud.notification.exception.handler

import jakarta.servlet.http.HttpServletRequest
import me.clud.notification.application.exception.NotificationException
import me.clud.notification.logger
import me.clud.notification.toKST
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class ExceptionResponse(
  val message: String,
  val status: HttpStatus,
  val cause: Throwable?,
  val path: String,
) {
  val timestamp = OffsetDateTime.now().toKST().format(DateTimeFormatter.ofPattern("yyyy.MM.dd'T'HH:mm:ss.SSS+09:00"))
}

data class ErrorResponse(
  val message: String,
  val status: String,
  val cause: Throwable?,
)

data class ErrorResponseMessage(
  val error: ErrorResponse,
  val path: String,
  val timestamp: String,
)

@RestControllerAdvice
class ExceptionHandler {
  private val logger = logger()

  private fun createErrorResponse(
    exceptionResponse: ExceptionResponse,
    e: Throwable? = null
  ): ResponseEntity<ErrorResponseMessage> {
    e?.let { logger.error(e.message, e) }

    return ResponseEntity(
      ErrorResponseMessage(
        error = ErrorResponse(
          message = exceptionResponse.message,
          status = exceptionResponse.status.reasonPhrase,
          cause = exceptionResponse.cause
        ),
        path = exceptionResponse.path,
        timestamp = exceptionResponse.timestamp
      ),
      exceptionResponse.status
    )
  }

  @ExceptionHandler(value = [NotificationException::class])
  fun handleDomainException(
    e: NotificationException,
    request: WebRequest,
    httpServletRequest: HttpServletRequest
  ) = createErrorResponse(
    ExceptionResponse(
      message = e.message,
      status = e.statusCode,
      cause = e.cause,
      path = httpServletRequest.requestURI,
    ),
    e
  )

  @ExceptionHandler(value = [BindException::class])
  fun handleNotValidArgumentException(
    e: BindException,
    result: BindingResult,
    httpServletRequest: HttpServletRequest
  ) = createErrorResponse(
    ExceptionResponse(
      message = e.bindingResult.fieldError?.defaultMessage ?: "잘못된 요청이 있습니다",
      status = HttpStatus.BAD_REQUEST,
      cause = e.cause,
      path = httpServletRequest.requestURI,
    )
  )

  @ExceptionHandler(value = [HttpMessageNotReadableException::class])
  fun handleNotReadableException(
    e: HttpMessageNotReadableException,
    httpServletRequest: HttpServletRequest
  ) = createErrorResponse(
    ExceptionResponse(
      message = "요청 전문이 잘못됐습니다.",
      status = HttpStatus.BAD_REQUEST,
      cause = e.cause,
      path = httpServletRequest.requestURI,
    )
  )

  @ExceptionHandler(value = [Exception::class])
  fun handleException(
    e: Exception,
    httpServletRequest: HttpServletRequest
  ) = createErrorResponse(
    ExceptionResponse(
      message = e.message ?: "잠시 후 다시 시도해주시기 바랍니다.",
      status = HttpStatus.INTERNAL_SERVER_ERROR,
      cause = e.cause,
      path = httpServletRequest.requestURI,
    ),
    e
  )
}
