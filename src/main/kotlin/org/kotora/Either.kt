package org.kotora

sealed class Either<L, R> {

    data class Right<L, R>(val right: R) : Either<L, R>()
    data class Left<L, R>(val left: L) : Either<L, R>()

    companion object {
        fun <L, R> right(right: R): Either<L, R> {
            return Right(right)
        }

        fun <L, R> left(left: L): Either<L, R> {
            return Left(left)
        }
    }

    inline fun <T> fold(leftHandler: (L) -> T, rightHandler: (R) -> T): T {
        return when (this) {
            is Left -> leftHandler(left)
            is Right -> rightHandler(right)
        }
    }

    inline fun <R1> mapRight(transform: (R) -> R1): Either<L, R1> {
        return when (this) {
            is Left -> left(left)
            is Right -> right(transform(right))
        }
    }

    inline fun <L1> mapLeft(transform: (L) -> L1): Either<L1, R> {
        return when (this) {
            is Left -> left(transform(left))
            is Right -> right(right)
        }
    }

    fun getRightOrNull(): R? {
        return if (this is Right) {
            right
        } else {
            null
        }
    }

    fun getLeftOrNull(): L? {
        return if (this is Left) {
            left
        } else {
            null
        }
    }

    fun isRight() = this is Right

    fun isLeft() = this is Left

    fun swap(): Either<R, L> {
        return when (this) {
            is Left -> right(left)
            is Right -> left(right)
        }
    }

    inline fun <R1> flatMapRight(transform: (R) -> Either<L, R1>): Either<L, R1> {
        return when (this) {
            is Left -> left(left)
            is Right -> transform(right)
        }
    }

    inline fun <L1> flatMapLeft(transform: (L) -> Either<L1, R>): Either<L1, R> {
        return when (this) {
            is Left -> transform(left)
            is Right -> right(right)
        }
    }
}

