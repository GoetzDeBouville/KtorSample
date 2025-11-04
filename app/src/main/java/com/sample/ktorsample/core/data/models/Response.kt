package com.sample.ktorsample.core.data.models

/**
 * DO #5
 *
 * Добавляем бавзовые модели сетевого клиента
 * */
open class Response<T>(
    var resultCode: StatusCode = StatusCode(0),
    var body: T? = null
)
