package me.clud.notification

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun OffsetDateTime.toKST(): OffsetDateTime = this.withOffsetSameInstant(ZoneOffset.ofHours(9))

inline fun <reified T> T.logger(): Logger {
  return LoggerFactory.getLogger(T::class.java)
}

suspend fun <T> CompletableFuture<T>.await(): T =
  suspendCoroutine <T> { cont: Continuation<T> ->
    whenComplete { result, exception ->
      if (exception == null) // the future has been completed normally
        cont.resume(result)
      else // the future has completed with an exception
        cont.resumeWithException(exception)
    }
  }
