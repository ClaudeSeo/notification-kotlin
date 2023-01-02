package me.clud.notification.application.configuration

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

@Configuration
class MongoConfiguration(
  @Value("\${spring.data.mongodb.database}")
  private val database: String,

  @Value("\${spring.data.mongodb.uri}")
  private val uri: String
) : AbstractMongoClientConfiguration() {

  override fun getDatabaseName(): String {
    return database
  }

  override fun mongoClient(): MongoClient {
    val connectionString = ConnectionString(uri)
    val settings = MongoClientSettings.builder().applyConnectionString(connectionString).build()
    return MongoClients.create(settings)
  }
}
