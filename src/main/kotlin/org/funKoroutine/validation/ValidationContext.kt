package org.funKoroutine.validation

import kotlin.reflect.KProperty0

class ValidationContext {

    val errors = mutableListOf<ValidationError<*>>()

    infix fun <R> KProperty0<R?>.`must be`(validationPredicate: (R?) -> ValidationResult) {
        validationPredicate(get())
            .takeUnless { it.outcome.affirmative }
            ?.also {
                errors.add(ValidationError(this, it.outcome, it.expected))
            }
    }

    infix fun <R : Any?> KProperty0<R?>.`must not be`(validationPredicate: (R?) -> ValidationResult) {
        validationPredicate(get())
            .takeIf { it.outcome.affirmative }
            ?.also {
                errors.add(ValidationError(this, it.outcome, it.expected))
            }
    }

    suspend infix fun <R> KProperty0<R?>.`must be`(validationPredicate: suspend (R?) -> ValidationResult) {
        validationPredicate(get())
            .takeIf { it.outcome.affirmative }
            ?.also {
                errors.add(ValidationError(this, it.outcome, it.expected))
            }
    }

    suspend infix fun <R : Any?> KProperty0<R?>.`must not be`(validationPredicate: suspend (R?) -> ValidationResult) {
        validationPredicate(get())
            .takeUnless { it.outcome.affirmative }
            ?.also {
                errors.add(ValidationError(this, it.outcome, it.expected))
            }
    }
}