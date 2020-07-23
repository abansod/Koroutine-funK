package org.kotora.validation

import kotlin.reflect.KProperty0
import kotlin.reflect.KType

class Validation(val errors: List<ValidationError<*>>, val hasError: Boolean = errors.isNotEmpty())

enum class DefaultOutcome(override val description: String, override val affirmative: Boolean) : Outcome {
    EQUAL("EQUAL TO", true),
    NOT_EQUAL("NOT EQUAL TO", false),
    GREATER_THAN("GREATER THAN", true),
    NOT_GREATER_THAN("NOT GREATER THAN", false),
    GREATER_THAN_OR_EQUAL("GREATER THAN OR EQUAL TO", true),
    NOT_GREATER_THAN_OR_EQUAL("NOT GREATER THAN OR EQUAL TO", false),
    LESS_THAN("LESS THAN", true),
    NOT_LESS_THAN("NOT LESS THAN", false),
    LESS_THAN_OR_EQUAL("LESS THAN OR EQUAL TO", true),
    NOT_LESS_THAN_OR_EQUAL("NOT LESS THAN OR EQUAL TO", false),
    FOLLOWING_PATTERN("FOLLOWING PATTERN", true),
    NOT_FOLLOWING_PATTERN("NOT FOLLOWING PATTERN", false),
    SORTED("SORTED BY", true),
    NOT_SORTED("NOT SORTED BY", false),
    SORTED_DESCENDING("SORTED DESCENDING BY", true),
    NOT_SORTED_DESCENDING("NOT SORTED DESCENDING BY", false),
    NULL("NULL", true),
    NOT_NULL("NOT NULL", false);

    override fun compliment(): Outcome {
        return when (this) {
            EQUAL -> NOT_EQUAL
            GREATER_THAN -> NOT_GREATER_THAN
            GREATER_THAN_OR_EQUAL -> NOT_GREATER_THAN_OR_EQUAL
            LESS_THAN -> NOT_LESS_THAN
            LESS_THAN_OR_EQUAL -> NOT_LESS_THAN_OR_EQUAL
            FOLLOWING_PATTERN -> NOT_FOLLOWING_PATTERN
            NULL -> NOT_NULL
            NOT_EQUAL -> EQUAL
            NOT_GREATER_THAN -> GREATER_THAN
            NOT_GREATER_THAN_OR_EQUAL -> GREATER_THAN_OR_EQUAL
            NOT_LESS_THAN -> LESS_THAN
            NOT_LESS_THAN_OR_EQUAL -> LESS_THAN_OR_EQUAL
            NOT_FOLLOWING_PATTERN -> FOLLOWING_PATTERN
            NOT_NULL -> NULL
            SORTED -> NOT_SORTED
            NOT_SORTED -> SORTED
            SORTED_DESCENDING -> NOT_SORTED_DESCENDING
            NOT_SORTED_DESCENDING -> SORTED_DESCENDING
        }
    }
}

interface Outcome {
    val name: String
    val description: String
    val affirmative: Boolean
    fun compliment(): Outcome
}

class ValidationError<R>(
    property: KProperty0<R?>,
    val outcome: Outcome,
    val expected: R?
) {
    val fieldName: String = property.name
    val fieldValue: R? = property.get()
    val fieldType: KType = property.returnType



    override fun toString(): String {
        return "$fieldValue -> $fieldName IS ${outcome.description} $expected"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ValidationError<*>

        if (outcome != other.outcome) return false
        if (expected != other.expected) return false
        if (fieldName != other.fieldName) return false
        if (fieldValue != other.fieldValue) return false
        if (fieldType != other.fieldType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = outcome.hashCode()
        result = 31 * result + (expected?.hashCode() ?: 0)
        result = 31 * result + fieldName.hashCode()
        result = 31 * result + (fieldValue?.hashCode() ?: 0)
        result = 31 * result + fieldType.hashCode()
        return result
    }
}

inline fun validate(function: ValidationContext.() -> Unit): Validation {
    val validationContext = ValidationContext()
    function(validationContext)
    return Validation(validationContext.errors)
}

data class ValidationResult(val outcome: Outcome, val expected: String = "")