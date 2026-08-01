package com.devhjs.plantdex.core.util

sealed class Result<out T, out E> {
    data class Success<out T>(
        val data: T
    ) : Result<T, Nothing>()

    data class Error<out E>(
        val error: E
    ) : Result<Nothing, E>()
}

/** inline 이라서 suspend 함수를 람다 안에서 호출할 수 있다. 떼면 코루틴 컨텍스트가 끊긴다. */
inline fun <T, R, E> Result<T, E>.map(transform: (T) -> R): Result<R, E> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error -> this
}
