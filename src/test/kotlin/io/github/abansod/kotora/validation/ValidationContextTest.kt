package io.github.abansod.kotora.validation

import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import java.net.Inet4Address
import java.net.InetAddress

internal class ValidationContextTest {

    @Test
    fun `should validate`() {

        val user = User(
            "Arya",
            39,
            "9876543212",
            listOf("arya@company.com"),
            Inet4Address.getLocalHost(),
            null
        )

        val validate = validate {
            user::alternateMobile `must be` `equal to null`()

            user::alternateMobile `must not be` `equal to null`()

            user::alternateMobile `must be` `equal to` { "" }

            user::alternateMobile `must be` `greater than` { "none" }

            user::alternateMobile `must not be` `greater than or equal` { "" }

            user::name `must be` `equal to` { "Arya" }

            user::email `must be` `sorted by` { it: String -> it.length }

            user::mobile `must not be` `equal to` { "9876543212" }

        }

        validate.hasError shouldBe true

        validate.errors.shouldHaveSize(4)

        validate.errors.shouldContainExactly(
            ValidationError(user::alternateMobile,DefaultOutcome.NULL,""),
            ValidationError(user::alternateMobile,DefaultOutcome.NOT_EQUAL,""),
            ValidationError(user::alternateMobile,DefaultOutcome.NOT_GREATER_THAN,"none"),
            ValidationError(user::mobile,DefaultOutcome.EQUAL,"9876543212")
        )
    }
}


class User(val name: String, val age: Int, val mobile: String, val email: List<String>, val ipAddress: InetAddress, val alternateMobile: String?)