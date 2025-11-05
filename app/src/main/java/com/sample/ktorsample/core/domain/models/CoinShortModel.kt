package com.sample.ktorsample.core.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DO #11
 *
 *  Создаем модели данных для фичи.
 *
 * */
@Serializable
data class CoinShortModel(
    @SerialName("symbol") val symbol: String,
    @SerialName("price") val price: String
)
