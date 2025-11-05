package com.sample.ktorsample.feature.data.network.impl.models

/**
 * DO #12
 *
 * Типизированные запросы.
 *
 * Примечание:
 *  - Для всех цен сразу символ не передаём (вернётся массив всех пар).
 *  - Для конкретного символа добавляем query-параметр symbol (например, BTCUSDT).
 */
sealed interface BinanceRequest {

    /**
     * Путь эндпоинта
     * */
    val path: String

    /**
     * Список цен для всех символов.
     */
    data object GetAllPrices : BinanceRequest {
        override val path: String = "/api/v3/ticker/price"
    }

    /**
     * Суточная статистика по символу.
     */
    data class Get24hTicker(val symbol: String) : BinanceRequest {
        override val path: String = "/api/v3/ticker/24hr"
    }
}