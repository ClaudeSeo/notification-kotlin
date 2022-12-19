package me.clud.notification.configuration

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
class RedisConfiguration(
  @Value("\${spring.redis.host}")
  private val redisHost: String,

  @Value("\${spring.redis.port}")
  private val redisPort: Int,

  @Value("\${spring.redis.lettuce.pool.max-idle}")
  private val redisMaxIdle: Int,

  @Value("\${spring.redis.lettuce.pool.min-idle}")
  private val redisMinIdle: Int,

  @Value("\${spring.redis.lettuce.pool.max-active}")
  private val redisMaxActive: Int,

  @Value("\${spring.redis.lettuce.pool.max-idle}")
  private val redisMaxWait: Int,
) : CachingConfigurer {

  private fun lettuceFactory(
    hostName: String,
    port: Int,
    timeout: Long,
    database: Int,
  ): LettuceConnectionFactory {
    val factory = LettuceConnectionFactory(
      RedisStandaloneConfiguration(hostName, port),
      LettucePoolingClientConfiguration.builder()
        .commandTimeout(Duration.ofMillis(timeout))
        .poolConfig(
          GenericObjectPoolConfig<Any>().apply {
            minIdle = redisMinIdle.toInt()
            maxIdle = redisMaxIdle.toInt()
            maxTotal = redisMaxActive.toInt()
            setMaxWait(Duration.ofSeconds(redisMaxWait.toLong()))
          }
        ).build()
    )
    factory.database = database
    return factory
  }

  override fun cacheManager(): CacheManager? {
    val objectMapper = ObjectMapper()
      .registerModule(ParameterNamesModule(JsonCreator.Mode.DEFAULT))
      .registerModule(Jdk8Module())
      .registerModule(JavaTimeModule())
      .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    objectMapper.activateDefaultTyping(
      objectMapper.polymorphicTypeValidator, DefaultTyping.EVERYTHING, As.PROPERTY
    )

    val configuration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeKeysWith(
        RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer())
      )
      .serializeValuesWith(
        RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer(objectMapper))
      )
      .entryTtl(Duration.ofMinutes(10))

    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory())
      .cacheDefaults(configuration).build()
  }

  @Primary
  @Bean
  fun connectionFactory(): RedisConnectionFactory {
    return lettuceFactory(
      hostName = redisHost,
      port = redisPort,
      timeout = RedisSetting.MAIN_REDIS.writeTimeout,
      database = RedisSetting.MAIN_REDIS.database
    )
  }

  enum class RedisSetting(
    val database: Int,
    val readTimeout: Long,
    val writeTimeout: Long,
  ) {
    MAIN_REDIS(database = 0, readTimeout = 1000, writeTimeout = 1500)
  }
}
