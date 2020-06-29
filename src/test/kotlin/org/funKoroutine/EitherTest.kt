package org.funKoroutine

import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test


@ExperimentalCoroutinesApi
internal class EitherTest {

    val incrementInLong: suspend (Int) -> Long = { it + 1L }
    val decrementInLong: suspend (Int) -> Long = { it - 1L }
    val tenDivideBy: suspend (Int) -> Either<Throwable, Int> = {
        try {
            Either.right(10 / it)
        } catch (e: Exception) {
            Either.left(e)
        }
    }
    val exceptionHandler: suspend (Throwable) -> Either<Int, Int> = { Either.left(0) }

    @Test
    fun `should fold the either`() = runBlockingTest {
        Either.right<Int, Int>(2).fold({ incrementInLong(it) }) { decrementInLong(it) } shouldBe 1L

        Either.left<Int, Int>(2).fold({ incrementInLong(it) }) { decrementInLong(it) } shouldBe 3L

    }


    @Test
    fun `should fold the either right with suspended handler`() = runBlockingTest {
        val either = Either.right<Int, Int>(2)
        val rightHandler: suspend (Int) -> Int = { it + 5 }
        either.fold({ it + 2 }) { rightHandler(it) } shouldBe 7
    }


    @Test
    fun `should transform using map`() = runBlockingTest {
        Either.left<Int, Int>(2).mapLeft { incrementInLong(it) } shouldBe Either.left<Long, Int>(3)

        Either.right<Int, Int>(2).mapLeft { incrementInLong(it) } shouldBe Either.right<Long, Int>(2)

        Either.left<Int, Int>(2).mapRight { incrementInLong(it) } shouldBe Either.left<Int, Long>(2)

        Either.right<Int, Int>(2).mapRight { incrementInLong(it) } shouldBe Either.right<Int, Long>(3)
    }

    @Test
    fun `should get Right or null`() {
        Either.left<Int, Long>(2).getRightOrNull() shouldBe null

        Either.right<Int, String>("right").getRightOrNull() shouldBe "right"
    }

    @Test
    fun `should get left or null`() {
        Either.left<Int, Long>(2).getLeftOrNull() shouldBe 2

        Either.right<Int, String>("right").getLeftOrNull() shouldBe null
    }

    @Test
    fun `should swap the components`() {
        Either.left<String, Long>("2").swap() shouldBe Either.right<Long, String>("2")
    }

    @Test
    fun `should get isRight`() {
        Either.left<Int, Long>(2).isRight() shouldBe false

        Either.right<Int, String>("right").isRight() shouldBe true
    }

    @Test
    fun `should get isLeft`() {
        Either.left<Int, Long>(2).isLeft() shouldBe true

        Either.right<Int, String>("right").isLeft() shouldBe false
    }

    @Test
    fun `should flatMapRight`() = runBlockingTest {
        Either.right<Throwable, Int>(2)
            .flatMapRight { tenDivideBy(it) } shouldBe Either.right<Throwable, Int>(5)

        Either.right<Throwable, Int>(0)
            .flatMapRight { tenDivideBy(it) }
            .getLeftOrNull().shouldBeTypeOf<ArithmeticException>()

        Either.left<Throwable, Int>(RuntimeException())
            .flatMapRight { tenDivideBy(it) }.getLeftOrNull().shouldBeTypeOf<RuntimeException>()
    }


    @Test
    fun `should flatMapLeft`() = runBlockingTest {
        Either.left<Throwable, Int>(RuntimeException())
            .flatMapLeft { exceptionHandler(it) } shouldBe Either.left<Int, Int>(0)

        Either.right<Throwable, Int>(2)
            .flatMapLeft { exceptionHandler(it) } shouldBe Either.right<Int, Int>(2)
    }
}