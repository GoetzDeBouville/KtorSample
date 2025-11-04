package com.sample.ktorsample.core.data.models

import com.sample.ktorsample.core.domain.models.ErrorType

/**
 * DO #5
 *
 * Добавляем бавзовые модели сетевого клиента
 * */
@JvmInline
value class StatusCode(val code: Int)

fun StatusCode.mapToErrorType(): ErrorType {
    return when (code) {
        NetworkParams.NO_CONNECTION_CODE -> ErrorType.NO_CONNECTION
        in NetworkParams.BAD_REQUEST_CODE..499 -> ErrorType.NO_CONNECTION
        in NetworkParams.SERVER_ERROR_CODE..599 -> ErrorType.SERVER_ERROR

        else -> ErrorType.UNKNOWN_ERROR
    }
}
