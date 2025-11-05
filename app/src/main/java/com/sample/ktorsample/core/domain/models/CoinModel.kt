package com.sample.ktorsample.core.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DO #11
 *
 *  Создаем модели данных для фичи.
 *
 *  Модели данных не нужно дублировать в DTO в data слое, если этого не требует формат данных.
 *  Поэтому достаточным будет прописать одну модель в domain и использовать во всех слоях без
 *  нарушения чистоты архитектуры.
 *
 *  Главное помнить что [Serializable] не является частью data слоя, api, sdk и т.д.
 *
 * Всегда лучше прописывать SerialName для каждого параметра, чтобы они не потерялись после
 * обфускации.
 * 
 * */
@Serializable
data class CoinModel(
    @SerialName("askPrice") val askPrice: String,
    @SerialName("askQty") val askQty: String,
    @SerialName("bidPrice") val bidPrice: String,
    @SerialName("bidQty") val bidQty: String,
    @SerialName("closeTime") val closeTime: Long,
    @SerialName("count") val count: Int,
    @SerialName("firstId") val firstId: Long,
    @SerialName("highPrice") val highPrice: String,
    @SerialName("lastId") val lastId: Long,
    @SerialName("lastPrice") val lastPrice: String,
    @SerialName("lastQty") val lastQty: String,
    @SerialName("lowPrice") val lowPrice: String,
    @SerialName("openPrice") val openPrice: String,
    @SerialName("openTime") val openTime: Long,
    @SerialName("prevClosePrice") val prevClosePrice: String,
    @SerialName("priceChange") val priceChange: String,
    @SerialName("priceChangePercent") val priceChangePercent: String,
    @SerialName("quoteVolume") val quoteVolume: String,
    @SerialName("symbol") val symbol: String,
    @SerialName("volume") val volume: String,
    @SerialName("weightedAvgPrice") val weightedAvgPrice: String
)
