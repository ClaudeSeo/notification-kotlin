package me.clud.notification

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun OffsetDateTime.toKST(): OffsetDateTime = this.withOffsetSameInstant(ZoneOffset.ofHours(9))

inline fun <reified T> T.logger(): Logger {
  return LoggerFactory.getLogger(T::class.java)
}
