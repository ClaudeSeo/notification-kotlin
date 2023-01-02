package me.clud.notification.infrastructure.user

import me.clud.notification.domain.user.entity.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId

@Document(collection = "users")
data class UserDataModel(
  @MongoId
  val id: ObjectId,
  val email: String,
  val phoneNumber: String,
)

fun UserDataModel.toEntity() = User(
  id = id.toString(),
  email = email,
  phoneNumber = phoneNumber
)
