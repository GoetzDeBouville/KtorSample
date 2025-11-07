package com.sample.ktorsample.core.data.models

/**
 * DO #5
 *
 * Добавляем бавзовые модели сетевого клиента
 * */
open class Response<SealedResponse>(
    var resultCode: StatusCode = StatusCode(0),
    var body: SealedResponse? = null
)
