package org.kotora.validation

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test

internal class ValidationPredicateKtTest {

    @Test
    fun `should return equal to predicate`() {
        val equalTo10Predicate = `equal to` { 10 }
        equalTo10Predicate(10) shouldBe ValidationResult(DefaultOutcome.EQUAL, "10")
        equalTo10Predicate(20) shouldBe ValidationResult(DefaultOutcome.NOT_EQUAL, "10")
    }

    @Test
    fun `should return null predicate`() {
        val nullPredicate = `equal to null`<String>()
        nullPredicate(null) shouldBe ValidationResult(DefaultOutcome.NULL)
        nullPredicate("hello") shouldBe ValidationResult(DefaultOutcome.NOT_NULL)
    }

    @Test
    fun `should return greater than predicate`() {
        val greaterThan10Predicate = `greater than` { 10 }
        greaterThan10Predicate(11) shouldBe ValidationResult(DefaultOutcome.GREATER_THAN, "10")
        greaterThan10Predicate(10) shouldBe ValidationResult(DefaultOutcome.NOT_GREATER_THAN, "10")
        greaterThan10Predicate(9) shouldBe ValidationResult(DefaultOutcome.NOT_GREATER_THAN, "10")
    }

    @Test
    fun `should return greater than or equal predicate`() {
        val greaterThanOrEqualTo10Predicate = `greater than or equal` { 10 }
        greaterThanOrEqualTo10Predicate(11) shouldBe ValidationResult(DefaultOutcome.GREATER_THAN_OR_EQUAL, "10")
        greaterThanOrEqualTo10Predicate(10) shouldBe ValidationResult(DefaultOutcome.GREATER_THAN_OR_EQUAL, "10")
        greaterThanOrEqualTo10Predicate(9) shouldBe ValidationResult(DefaultOutcome.NOT_GREATER_THAN_OR_EQUAL, "10")
    }


    @Test
    fun `should return less than predicate`() {
        val lessThan10Predicate = `less than` { 10 }
        lessThan10Predicate(11) shouldBe ValidationResult(DefaultOutcome.NOT_LESS_THAN, "10")
        lessThan10Predicate(10) shouldBe ValidationResult(DefaultOutcome.NOT_LESS_THAN, "10")
        lessThan10Predicate(9) shouldBe ValidationResult(DefaultOutcome.LESS_THAN, "10")
    }

    @Test
    fun `should return less than or equal predicate`() {
        val lessThanOrEqualTo10Predicate = `less than or equal` { 10 }
        lessThanOrEqualTo10Predicate(11) shouldBe ValidationResult(DefaultOutcome.NOT_LESS_THAN_OR_EQUAL, "10")
        lessThanOrEqualTo10Predicate(10) shouldBe ValidationResult(DefaultOutcome.LESS_THAN_OR_EQUAL, "10")
        lessThanOrEqualTo10Predicate(9) shouldBe ValidationResult(DefaultOutcome.LESS_THAN_OR_EQUAL, "10")
    }

    @Test
    fun `should return sort by predicate`() {
        val sortStringByLengthPredicate = `sorted by`<String, Int> { it.length }
        sortStringByLengthPredicate(listOf()) shouldBe ValidationResult(DefaultOutcome.SORTED)
        sortStringByLengthPredicate(listOf("1", "12", "123")) shouldBe ValidationResult(DefaultOutcome.SORTED)
        sortStringByLengthPredicate(listOf("111", "12", "123")) shouldBe ValidationResult(DefaultOutcome.NOT_SORTED)
        sortStringByLengthPredicate(listOf("111", "12", "123")) shouldBe ValidationResult(DefaultOutcome.NOT_SORTED)
    }

    @Test
    fun `should return sort by descending predicate`() {
        val sortStringByLengthDescendingPredicate = `sorted by decending`<String, Int> { it.length }
        sortStringByLengthDescendingPredicate(listOf()) shouldBe ValidationResult(DefaultOutcome.SORTED_DESCENDING)
        sortStringByLengthDescendingPredicate(listOf("1", "12", "123")) shouldBe ValidationResult(DefaultOutcome.NOT_SORTED_DESCENDING)
        sortStringByLengthDescendingPredicate(listOf("111", "12", "123")) shouldBe ValidationResult(DefaultOutcome.NOT_SORTED_DESCENDING)
        sortStringByLengthDescendingPredicate(listOf("111", "12", "1")) shouldBe ValidationResult(DefaultOutcome.SORTED_DESCENDING)
    }
}