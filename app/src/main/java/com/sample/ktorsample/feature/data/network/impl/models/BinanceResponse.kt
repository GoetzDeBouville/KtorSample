package com.sample.ktorsample.feature.data.network.impl.models

import com.sample.ktorsample.core.domain.models.coins.CoinModel
import com.sample.ktorsample.core.domain.models.coins.CoinShortModel

/**
 * DO #12
 *
 * Типизированные ответы.
 *
 */
sealed interface BinanceResponse {

    /**
     * Успешный ответ со списком цен для нескольких символов (все *USDT и т.п.).
     * */
    class Prices(val value: List<CoinShortModel>) : BinanceResponse

    /**
     * Успешный ответ с суточной статистикой по одному символу.
     * */
    class Ticker24h(val value: CoinModel) : BinanceResponse
}