package io.github.abansod.kotora.validation


inline fun <R> `equal to`(crossinline block: () -> R?): (R?) -> ValidationResult {
    return { property: R? ->
        val expectedValue = block()
        evaluate(property, DefaultOutcome.EQUAL, "$expectedValue") { it == expectedValue }
    }
}

fun <R> `equal to null`(): (R?) -> ValidationResult {
    return { property: R? ->
        if (property == null) {
            ValidationResult(DefaultOutcome.NULL)
        } else {
            ValidationResult(DefaultOutcome.NOT_NULL)
        }
    }
}

inline fun <R : Comparable<R>> `greater than`(crossinline block: () -> R): (R?) -> ValidationResult {
    return { property: R? ->
        val expected = block()
        evaluate(property, DefaultOutcome.GREATER_THAN, "$expected") { it > expected }
    }
}

inline fun <R : Comparable<R>> `greater than or equal`(crossinline block: () -> R): (R?) -> ValidationResult {
    return { property: R? ->
        val expected = block()
        evaluate(property, DefaultOutcome.GREATER_THAN_OR_EQUAL, "$expected") { it >= expected }
    }
}

inline fun <R : Comparable<R>> `less than`(crossinline block: () -> R): (R?) -> ValidationResult {
    return { property: R? ->
        val expected = block()
        evaluate(property, DefaultOutcome.LESS_THAN, "$expected") { it < expected }
    }
}

inline fun <R : Comparable<R>> `less than or equal`(crossinline block: () -> R): (R?) -> ValidationResult {
    return { property: R? ->
        val expected = block()
        evaluate(property, DefaultOutcome.LESS_THAN_OR_EQUAL, "$expected") { it <= expected }
    }
}

inline fun <E, T : Comparable<T>> `sorted by`(crossinline selector: (E) -> T): (Iterable<E>?) -> ValidationResult {
    return { property: Iterable<E>? ->
        evaluate(property, DefaultOutcome.SORTED) { it == it.sortedBy(selector) }
    }
}

inline fun <E, T : Comparable<T>> `sorted by decending`(crossinline selector: (E) -> T): (Iterable<E>?) -> ValidationResult {
    return { property: Iterable<E>? ->
        evaluate(property, DefaultOutcome.SORTED_DESCENDING) { it == it.sortedByDescending(selector) }
    }
}

fun <R> evaluate(
    property: R?,
    outcome: DefaultOutcome,
    expectationInString: String = "",
    predicate: (R) -> Boolean
): ValidationResult {
    val result = property?.let(predicate) ?: false
    return if (result) {
        ValidationResult(outcome, expectationInString)
    } else {
        ValidationResult(outcome.compliment(), expectationInString)
    }
}
