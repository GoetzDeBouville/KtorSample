package com.sample.ktorsample.core.domain.models

/**
 * DO #5
 *
 * Добавляем бавзовые модели сетевого клиента
 * */
sealed class Result<T, E> {

    data class Success<T, E>(val data: T) : Result<T, E>()

    data class Error<T, E>(val error: E) : Result<T, E>()
}