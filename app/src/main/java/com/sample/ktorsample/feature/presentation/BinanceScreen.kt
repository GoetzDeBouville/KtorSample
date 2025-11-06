package com.sample.ktorsample.feature.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.ktorsample.core.domain.models.coins.CoinShortModel
import com.sample.ktorsample.core.presentation.uikit.BinanceError
import com.sample.ktorsample.core.presentation.uikit.BinanceLoading
import com.sample.ktorsample.core.presentation.uikit.PriceRow
import com.sample.ktorsample.core.presentation.uikit.TickerDialog
import com.sample.ktorsample.feature.presentation.BinanceViewModel.Intent
import com.sample.ktorsample.ui.theme.KtorSampleTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BinanceScreen(
    modifier: Modifier,
    viewModel: BinanceViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Binance — цены") })
        }
    ) { padding ->
        Box(
            modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when {
                state.isLoadingPrices && state.prices.isEmpty() ->
                    BinanceLoading(
                        modifier = Modifier.fillMaxSize()
                    )

                state.error != null && state.prices.isEmpty() ->
                    BinanceError(
                        modifier = Modifier.fillMaxSize(),
                        error = state.error!!,
                        onRetry = { viewModel.accept(Intent.LoadAllPrices) },
                        onDismiss = { viewModel.accept(Intent.ClearError) }
                    )

                else ->
                    BinanceContent(
                        modifier = Modifier.fillMaxSize(),
                        prices = state.prices,
                        isRefreshing = state.isLoadingPrices,
                        onRefresh = { viewModel.accept(Intent.LoadAllPrices) },
                        onSymbolClick = { symbol ->
                            viewModel.accept(Intent.SelectSymbol(symbol))
                        }
                    )
            }

            if (state.isTickerDialogVisible && state.selectedSymbol != null) {
                TickerDialog(
                    symbol = state.selectedSymbol!!,
                    ticker = state.ticker,
                    isLoading = state.isLoadingTicker,
                    error = state.error,
                    onRetry = { viewModel.accept(Intent.RefreshTicker) },
                    onDismiss = {
                        viewModel.accept(Intent.DismissTickerDialog)
                        viewModel.accept(Intent.ClearError)
                    }
                )
            }
        }
    }
}

@Composable
private fun BinanceContent(
    modifier: Modifier,
    prices: List<CoinShortModel>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onSymbolClick: (String) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { onRefresh() },
        state = pullToRefreshState,
        modifier = modifier,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = pullToRefreshState
            )
        }
    ) {
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(items = prices, key = { it.symbol }) { item ->
                    PriceRow(
                        item = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSymbolClick(item.symbol) }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                    HorizontalDivider()
                }
            }
            if (isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(12.dp)
                        .size(20.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

@Preview(
    name = "BinanceContent — Default — Pixel",
    showBackground = true,
    device = Devices.PIXEL
)
@Composable
private fun Preview_BinanceContent_Default() {
    KtorSampleTheme {
        BinanceContent(
            modifier = Modifier.fillMaxSize(),
            prices = listOf(
                CoinShortModel(symbol = "BTCUSDT", price = "64210.50"),
                CoinShortModel(symbol = "ETHUSDT", price = "3120.10"),
                CoinShortModel(symbol = "SOLUSDT", price = "178.42"),
            ),
            isRefreshing = false,
            onRefresh = {},
            onSymbolClick = {}
        )
    }
}

@Preview(
    name = "BinanceContent — Refreshing — Pixel",
    showBackground = true,
    device = Devices.PIXEL
)
@Composable
private fun Preview_BinanceContent_Refreshing() {
    KtorSampleTheme {
        BinanceContent(
            modifier = Modifier.fillMaxSize(),
            prices = emptyList(),
            isRefreshing = true,
            onRefresh = {},
            onSymbolClick = {}
        )
    }
}
