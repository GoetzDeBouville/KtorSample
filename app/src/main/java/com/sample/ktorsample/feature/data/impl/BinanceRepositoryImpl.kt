package com.sample.ktorsample.feature.data.impl

import com.sample.ktorsample.core.domain.models.ErrorType
import com.sample.ktorsample.core.domain.models.Result
import com.sample.ktorsample.core.domain.models.coins.CoinModel
import com.sample.ktorsample.core.domain.models.coins.CoinShortModel
import com.sample.ktorsample.feature.data.network.api.BinanceRemoteDataSource
import com.sample.ktorsample.feature.domain.api.BinanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * DO #16
 *
 * Реализация интерфейса репозитория для binance.
 * В данном приемере репозиторий только пропускает поток.
 */
class BinanceRepositoryImpl @Inject constructor(
    private val binanceRemoteDataSource: BinanceRemoteDataSource
) : BinanceRepository {
    override fun getAllPrices(): Flow<Result<List<CoinShortModel>, ErrorType>> {
        return binanceRemoteDataSource.getAllPrices()
    }

    override fun get24hTicker(symbol: String): Flow<Result<CoinModel, ErrorType>> {
        return binanceRemoteDataSource.get24hTicker(symbol)
    }
}
