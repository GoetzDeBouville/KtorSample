package com.sample.ktorsample.feature.binance.data.network.impl

import android.content.Context
import com.sample.ktorsample.core.data.HttpKtorNetworkClient
import com.sample.ktorsample.core.domain.models.coins.CoinModel
import com.sample.ktorsample.core.domain.models.coins.CoinShortModel
import com.sample.ktorsample.feature.binance.data.network.impl.models.BinanceRequest
import com.sample.ktorsample.feature.binance.data.network.impl.models.BinanceResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.encodedPath
import javax.inject.Inject

/**
 * DO #13
 *
 * Ktor-клиент для API Binance.
 *
 * Принимает [HttpClient] c уже настроенным baseUrl (di определит нужного клиента по Qualifier
 * и типам для запросов и ответов).
 *
 * Класс наследуется от [HttpKtorNetworkClient], и реализует абстрактные методы [sendRequestByType] и
 * [getResponseBodyByRequestType].
 *
 */
class BinanceApi @Inject constructor(
    @ApplicationContext context: Context,
    private val httpClient: HttpClient
) : HttpKtorNetworkClient<BinanceRequest, BinanceResponse>(context) {

    /**
     * Внутри стэйтмашина. Компилятор всегда будет указывать если пропущен наследник класса запроса.
     */
    override suspend fun sendRequestByType(request: BinanceRequest): HttpResponse =
        when (request) {
            is BinanceRequest.GetAllPrices -> httpClient.get {
                url { encodedPath = request.path }
            }

            is BinanceRequest.Get24hTicker -> httpClient.get {
                url {
                    encodedPath = request.path
                    parameters.append("symbol", request.symbol)
                }
            }
        }

    /**
     * Аналогичная стэйтмашина для ответов.
     */
    override suspend fun getResponseBodyByRequestType(
        requestType: BinanceRequest,
        httpResponse: HttpResponse
    ): BinanceResponse =
        when (requestType) {
            is BinanceRequest.GetAllPrices -> {
                val items: List<CoinShortModel> = httpResponse.body()
                BinanceResponse.Prices(items)
            }

            is BinanceRequest.Get24hTicker -> {
                val ticker: CoinModel = httpResponse.body()
                BinanceResponse.Ticker24h(ticker)
            }
        }
}
