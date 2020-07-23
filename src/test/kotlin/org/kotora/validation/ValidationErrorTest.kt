package org.kotora.validation

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test

internal class ValidationErrorTest {

    @Test
    fun `should get string representation of Validation Error`() {
        val dummy = "dummy"
        val validationError = ValidationError(dummy::length, DefaultOutcome.NOT_EQUAL, 3)
        validationError.toString() shouldBe "5 -> length IS NOT EQUAL TO 3"
    }
}