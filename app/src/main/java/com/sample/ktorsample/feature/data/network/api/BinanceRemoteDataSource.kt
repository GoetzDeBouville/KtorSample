package com.sample.ktorsample.feature.data.network.api

import com.sample.ktorsample.core.domain.models.coins.CoinModel
import com.sample.ktorsample.core.domain.models.coins.CoinShortModel
import kotlinx.coroutines.flow.Flow

/**
 * DO #14
 *
 * Интерфейс обертки над реализованным клиентом.
 *
 * Он будет использоваться в репозитории. Главная задача обертки над клиентом это снизить нагрузку
 * с репозитория, и отдавать в репозиторий чистые данные.
 *
 * Определим сразу те методы, которые нам понадобятся в репозитории.
 */
interface BinanceRemoteDataSource {
    fun getAllPrices(): Flow<List<CoinShortModel>>
    fun get24hTicker(symbol: String): Flow<CoinModel>
}
