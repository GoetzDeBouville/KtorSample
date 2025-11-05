package com.sample.ktorsample.core.data.models.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

/**
 * DO #9
 *
 * Добавляем фабрику для создания HttpClient.
 *
 * Фабрика понадобится для создания клиентов под разные сервисы и/или движки (если мы работаем с KMP)
 * и провайда через DI.
 *
 * В реальном проекте можно ограничиться методом одним методом create без класса.
 *
 * */
class HttpClientFactory @Inject constructor() {
    /**
     * Фабричный метод create
     *
     * В реальных проектах часто приходится работать с несколькими сервисами, поэтому в фабричный
     * метод мы передаем [baseUrl] для удобства провайда нужного клиента.
     *
     * В KMP проектах в параметры добавляем еще и engineFactory, на случай если необходимо
     * поддерживать Android и iOS в одном проекте. В случае если в KMP проекте поддерживается
     * только Android, JVM и WASM, в качестве движка будет достаточным использовать OkHttp.
     *
     * Для KMP метод будет соответственно таким:
     *
     * ```kotlin
     *     fun create(baseUrl: String, engineFactory: HttpClientEngineFactory<HttpClientEngineConfig>): HttpClient {
     *         return HttpClient(engineFactory) {
     *         // ...
     *         }
     *     }
     * ```
     *
     * */
    fun create(baseUrl: String): HttpClient {
        return HttpClient(Android) {
            /**
             * Блоков install может быть много, под разные конфигурации.
             *
             * Блока Logging достаточно для того, чтобы покрыть логированием все запросы и ответы,
             * поэтому не нужно дублировать его в каждом HttpClient как я уже указал в комментариях.
             *
             * Напоминаю, правильно настроенный Timber выполняет логирование только в debug сборках,
             * но не на релизных, что очень важно для производительности приложения.
             *
             * */
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.v("Logger Ktor => $message")
                    }
                }
                level = LogLevel.ALL
            }

            /**
             * В блоке HttpTimeout соответственно выставляет таймауты для разных сценариев.
             *
             * requestTimeoutMillis — общий таймаут на весь запрос (включая подключение и чтение),
             * по прошествию этого времени клиент вернет ошибку.
             *
             * connectTimeoutMillis - сколько ждём установления TCP-соединения. По прошествию
             * времени возвращает ошибку.
             *
             * дополнительно socketTimeoutMillis (для websocket). Смысл тот же что и с другими
             * таймаутами.
             *
             * */
            install(HttpTimeout) {
                requestTimeoutMillis = CONNECTION_TIME_OUT_10_SEC
                connectTimeoutMillis = CONNECTION_TIME_OUT_10_SEC
            }

            /**
             * ContentNegotiation необходим для настройки сериализаторов
             * */
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true
                        isLenient = true // более «мягкий» парсер для неидеального JSON
                        explicitNulls = false // не посылать null явно в JSON (уменьшает «шум»)
                        ignoreUnknownKeys = true // не падать, если сервер прислал лишние поля
                        encodeDefaults = true // в запросах учитывает дефолтные значения полей
                    }
                )
            }

            /**
             * Общие настройки для всех запросов: базовый URL, заголовки, типы контента, дефолтные параметры.
             *
             * */
            defaultRequest {
                url(baseUrl)
                contentType(ContentType.Application.Json) //  Указываем тип контента (сразу укажем как Json, чтобы не писать это на каждой его реализации)
                accept(ContentType.Application.Json) // указываем какой формат ответа ожидаем от сервера
                // можем так же параметризовать заголовки и query-параметры.
            }

            /**
             *
             * Дополнительно можно настроить:
             *
             * HttpRequestRetry, но это обязательно нужно соглаосовывать с командой бэкэнда.
             * Если не знаете нужно ли это вам, то лучше не добавлять блок для HttpRequestRetry.
             *
             * ```
             * install(HttpRequestRetry) {
             *     retryOnServerErrors(maxRetries = 2)
             *     retryOnException(maxRetries = 2)
             *     exponentialDelay()
             * }
             *```
             *
             * HttpCallValidator, нужен для автоматического маппинга кодов ответа сервера. Лучше в
             * самостоятельно конечно это делать.
             *
             * Auth, нужен для провайда токенов.
             * WebSockets
             * и тд
             *
             * */
        }
    }

    private companion object {
        const val CONNECTION_TIME_OUT_10_SEC = 10_000L
    }
}
