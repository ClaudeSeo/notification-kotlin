package me.clud.notification.annotation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EnumValidator::class])
annotation class ValueOfEnum(
  val enumClass: KClass<out Enum<*>>,
  val message: String = "",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = []
)

class EnumValidator : ConstraintValidator<ValueOfEnum, String> {
  private val acceptedValues: MutableList<String> = mutableListOf()

  override fun initialize(annotation: ValueOfEnum) {
    super.initialize(annotation)
    acceptedValues.addAll(
      annotation.enumClass.java
        .enumConstants
        .map { it.name }
    )
  }

  override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
    return if (value == null) {
      true
    } else acceptedValues.contains(value.toString())
  }
}
