package com.sample.ktorsample.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient

/**
 * DO #10
 *
 * Создаем и пробрасываем в зависимости клиента
 *
 * */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    /**
     * Для удобства всегда лучше создавать Qualifier для каждого клиента и его baseUrl.
     * В Текущем примере ограничимся упрощенным вариантом.
     *
     * */
    @Provides
    fun provideBinanceClient(
        factory: HttpClientFactory,
        baseUrl: String = "https://data-api.binance.vision/api/v3/"
    ): HttpClient {
        return factory.create(baseUrl)
    }
}
