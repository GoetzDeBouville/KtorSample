package com.sample.ktorsample.feature.domain.api

import com.sample.ktorsample.core.domain.models.ErrorType
import com.sample.ktorsample.core.domain.models.coins.CoinModel
import com.sample.ktorsample.core.domain.models.coins.CoinShortModel
import kotlinx.coroutines.flow.Flow
import com.sample.ktorsample.core.domain.models.Result

/**
 * DO #16
 *
 * Интерфейс репозитория для binance.
 */
interface BinanceRepository {
    fun getAllPrices(): Flow<Result<List<CoinShortModel>, ErrorType>>
    fun get24hTicker(symbol: String): Flow<Result<CoinModel, ErrorType>>
}
