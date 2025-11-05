package com.sample.ktorsample.feature.data.network.impl

import com.sample.ktorsample.core.data.HttpNetworkClient
import com.sample.ktorsample.core.domain.models.coins.CoinModel
import com.sample.ktorsample.core.domain.models.coins.CoinShortModel
import com.sample.ktorsample.feature.data.network.api.BinanceRemoteDataSource
import com.sample.ktorsample.feature.data.network.impl.models.BinanceRequest
import com.sample.ktorsample.feature.data.network.impl.models.BinanceResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.sample.ktorsample.core.data.models.Response
import com.sample.ktorsample.core.data.models.mapToErrorType
import com.sample.ktorsample.core.domain.models.ErrorType
import com.sample.ktorsample.core.domain.models.Result

/**
 * DO #15
 * Реализация обертку клиента.
 *
 * Обертка над клиентом позволяет снять нагрузку с репозитория по работе с сетевым клиентом.
 *
 */
class BinanceRemoteDataSourceImpl @Inject constructor(
    private val client: HttpNetworkClient<BinanceRequest, BinanceResponse>
) : BinanceRemoteDataSource {

    override fun getAllPrices(): Flow<Result<List<CoinShortModel>, ErrorType>> = flow {
        val resp: Response<BinanceResponse> =
            client.getResponse(BinanceRequest.GetAllPrices)

        when (val body = resp.body) {
            is BinanceResponse.Prices -> emit(Result.Success(body.value))
            else -> emit(Result.Error(resp.resultCode.mapToErrorType()))
        }
    }

    override fun get24hTicker(symbol: String): Flow<Result<CoinModel, ErrorType>> = flow {
        val resp: Response<BinanceResponse> =
            client.getResponse(BinanceRequest.Get24hTicker(symbol))

        when (val body = resp.body) {
            is BinanceResponse.Ticker24h -> emit(Result.Success(body.value))
            else -> emit(Result.Error(resp.resultCode.mapToErrorType()))
        }
    }
}
