package com.sample.ktorsample.core.presentation.utils

import com.sample.ktorsample.core.domain.models.ErrorType

fun ErrorType.toUiText(): String = when (this) {
    ErrorType.NO_CONNECTION -> "Нет подключения к интернету"
    ErrorType.SERVER_ERROR -> "Ошибка сервера"
    ErrorType.UNKNOWN_ERROR -> "Неизвестная ошибка"
}
