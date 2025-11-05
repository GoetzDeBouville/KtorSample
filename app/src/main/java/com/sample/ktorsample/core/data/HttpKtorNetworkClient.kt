package com.sample.ktorsample.core.data

import android.content.Context
import com.sample.ktorsample.core.data.models.Response
import com.sample.ktorsample.core.data.models.StatusCode
import com.sample.ktorsample.core.utils.isInternetReachable
import com.sample.ktorsample.core.utils.runSafely
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import timber.log.Timber

/**
 * DO #8
 * Реализуем [HttpNetworkClient] абстрактным классом [HttpKtorNetworkClient]
 *
 * Для того чтобы было ясно куда пробрасывать какой sealed класс (запрос или ответ), прописываем
 * дженерики с соответствующими именами, чтобы было ясно: один тип — это запрос ([SealedRequest]),
 * второй — ответ ([SealedResponse])..
 *
 * Класс делаем абстрактным, так как для каждой фичи будет отдельная реализация-обёртка,
 * выполняющая задачи конкретной фичи.
 *
 *
 * Класс содержит готовую реализацию [getResponse] — она общая для всех наследников:
 * принимает запрос и возвращает доменный отмапленный [Response].
 * */
abstract class HttpKtorNetworkClient<SealedRequest, SealedResponse>(
    @ApplicationContext private val context: Context
) : HttpNetworkClient<SealedRequest, SealedResponse> {
    /**
     * Универсальная точка входа для сетевого запроса.
     *
     * 1) Сначала проверяем интернет — нет смысла дергать сеть без подключения.
     * 2) Выполняем работу внутри [runSafely] — чтобы родительская корутина не пропускала исключение
     * [CancellationException].
     * 3) На успех — маппим сырой [HttpResponse] в доменный [Response] через [mapToResponse].
     * 4) На ошибку — логируем через Timber и возвращаем пустой [Response] или
     *    [Response] с кодом -1 при отсутствии интернета.
     *
     * На самом деле логирование тут излишне, достаточно его прописать в фабричной функции, но
     * например для трекинга ошибок с отправкой на удаленный сервер можно вместо логеров собирать
     * данные об ошибках и отложенно отправлять на свой сервер именно отсюда.
     *
     * В текущем примере логирование ошибки будет уже излишним, так как логирование будет
     * подключено в фабричной функции.
     * */
    override suspend fun getResponse(sealedRequest: SealedRequest): Response<SealedResponse> {
        return if (context.isInternetReachable()) {
            runSafely {
                mapToResponse(
                    requestType = sealedRequest,
                    httpResponse = sendRequestByType(sealedRequest)
                )
            }.onFailure { error ->
                Timber.v("error -> ${error.localizedMessage}")
            }.getOrNull() ?: Response()
        } else {
            Response(resultCode = StatusCode(-1))
        }
    }

    /**
     * Просто маппер.
     *
     * Конвертирует сырой [HttpResponse] в обёртку [Response].
     *
     * Логи так же излишни, см. доку к [getResponse]
     */
    private suspend fun mapToResponse(
        requestType: SealedRequest,
        httpResponse: HttpResponse
    ): Response<SealedResponse> {
        return if (httpResponse.status.isSuccess()) {
            try {
                Response(
                    resultCode = StatusCode(httpResponse.status.value),
                    body = getResponseBodyByRequestType(requestType, httpResponse)
                )
            } catch (e: SerializationException) {
                Timber.e(e.message.toString())
                Response(
                    resultCode = StatusCode(httpResponse.status.value)
                )
            } catch (e: IOException) {
                Timber.e(e.message.toString())
                Response(
                    resultCode = StatusCode(httpResponse.status.value)
                )
            }
        } else {
            Response(
                resultCode = StatusCode(httpResponse.status.value)
            )
        }
    }

    /**
     * Метод для отправления запроса.
     *
     * В теле метода у нас всегда будет стэйтмашина (точнее сказать что лучше реализовывать со
     * стэйтмашиной, пример будет реализован с sealed классом), таким образом метод будет выглядеть
     * максимально шаблонно.
     *
     * В реализациях этого метода для каждого клиента:
     * - вызываем соответствующий метод (GET/POST/PATCH и т.д.) http клиента,
     * - собираем URL и параметры,
     * - добавляем заголовки/тело,
     * - вызываем Ktor-клиент и получаем [HttpResponse].
     *
     */
    protected abstract suspend fun sendRequestByType(request: SealedRequest): HttpResponse

    /**
     * Метод для обработки возврата.
     *
     * В реализациях так же ожидается стэйтмашина, которая возвращает ответ типа [SealedResponse]
     *
     */
    protected abstract suspend fun getResponseBodyByRequestType(
        requestType: SealedRequest,
        httpResponse: HttpResponse
    ): SealedResponse
}
