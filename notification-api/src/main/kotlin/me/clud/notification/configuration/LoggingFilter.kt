package me.clud.notification.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.apache.logging.log4j.message.ObjectMessage
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.util.*

@Component
class LoggingFilter : OncePerRequestFilter() {
  private val ignoreLoggingRequestPath = listOf("/health")
  private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    MDC.put("traceId", UUID.randomUUID().toString())
    if (isAsyncDispatch(request)) {
      filterChain.doFilter(request, response)
    } else {
      doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain)
    }
  }

  protected fun doFilterWrapped(
    request: ContentCachingRequestWrapper,
    response: ContentCachingResponseWrapper,
    filterChain: FilterChain
  ) {
    var exception: Exception? = null
    val logData = LogData()
    val startTime = System.currentTimeMillis()

    try {
      filterChain.doFilter(request, response)
    } catch (e: Exception) {
      exception = e
      logData.logLevel = "ERROR"
      logData.errorCode = e.message
      logData.errorMessage = e.message
      logData.stackTrace = e.stackTrace.toString()
    } finally {
      response.copyBodyToResponse()
    }

    ignoreLoggingRequestPath.find { it -> it == request.requestURI }?.let { return }
    val endTime = System.currentTimeMillis()
    val requestBodyString = if (request.contentAsByteArray.isNotEmpty()) String(request.contentAsByteArray) else "{}"
    logData.body = Json.decodeFromString<JsonObject>(requestBodyString)
    logData.headers = request.headerNames.toList().map { it -> it to request.getHeader(it) }.toMap()
    logData.path = request.requestURI
    logData.queryString = request.queryString
    logData.method = request.method
    logData.statusCode = response.status
    logData.responseTime = endTime - startTime

    val log = ObjectMessage(ObjectMapper().readValue(Json.encodeToString(logData), TreeMap::class.java))

    try {
      if (exception != null) throw exception!!
      logger.info(log)
    } catch (e: Exception) {
      logger.error(log, e)
    } finally {
      MDC.clear()
    }
  }

  private fun wrapRequest(request: HttpServletRequest): ContentCachingRequestWrapper {
    if (request is ContentCachingRequestWrapper) return request
    return ContentCachingRequestWrapper(request)
  }

  private fun wrapResponse(response: HttpServletResponse): ContentCachingResponseWrapper {
    if (response is ContentCachingResponseWrapper) return response
    return ContentCachingResponseWrapper(response)
  }
}

@Serializable
data class LogData(
  var logLevel: String = "INFO",
  var body: JsonObject? = null,
  var headers: Map<String, String> = mapOf(),
  var path: String? = "",
  var queryString: String? = "",
  var method: String? = "",
  var errorMessage: String? = null,
  var errorCode: String? = null,
  var stackTrace: String? = null,
  var statusCode: Int? = null,
  var responseTime: Long? = null,
)
