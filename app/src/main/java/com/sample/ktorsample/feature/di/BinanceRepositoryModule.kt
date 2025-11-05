package com.sample.ktorsample.feature.di

import android.content.Context
import com.sample.ktorsample.core.data.HttpNetworkClient
import com.sample.ktorsample.feature.data.impl.BinanceRepositoryImpl
import com.sample.ktorsample.feature.data.network.api.BinanceRemoteDataSource
import com.sample.ktorsample.feature.data.network.impl.BinanceApi
import com.sample.ktorsample.feature.data.network.impl.BinanceRemoteDataSourceImpl
import com.sample.ktorsample.feature.data.network.impl.models.BinanceRequest
import com.sample.ktorsample.feature.data.network.impl.models.BinanceResponse
import com.sample.ktorsample.feature.domain.api.BinanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

/**
 * DO #17
 *
 * Провайд зависимостей до репозитория
 */
@Module
@InstallIn(SingletonComponent::class)
class BinanceRepositoryModule {
    @Provides
    @Singleton
    fun provideBinanceClient(
        @ApplicationContext context: Context,
        httpClient: HttpClient,
    ): BinanceApi = BinanceApi(
        context = context,
        httpClient = httpClient
    )

    @Provides
    fun provideBinanceHttpClient(client: BinanceApi):
            HttpNetworkClient<BinanceRequest, BinanceResponse> = client

    @Provides
    fun provideBinanceRemoteDataSource(impl: BinanceRemoteDataSourceImpl):
            BinanceRemoteDataSource = impl

    @Provides
    fun provideBinanceRepository(impl: BinanceRepositoryImpl): BinanceRepository = impl
}
