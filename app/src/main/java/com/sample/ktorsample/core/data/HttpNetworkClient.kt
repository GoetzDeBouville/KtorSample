package com.sample.ktorsample.core.data

import com.sample.ktorsample.core.data.models.Response

/**
 * DO #7
 * Интерфейс базового клиента
 *
 * В качестве дженериков в реализациях клиента будут выступать sealed классы для запроса и возврата.
 *
 * Единственная функция getResponse будет возвращать ответ в виде наследника класса Response,
 * который содержит всего два очевидных параметра - resultCode и body.
 * Тип для body приходит из дженерика класса.
 * */
interface HttpNetworkClient<T, R> {
    suspend fun getResponse(sealedRequest: T): Response<R>
}
