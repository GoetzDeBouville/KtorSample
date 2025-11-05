package com.sample.ktorsample.feature.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sample.ktorsample.core.domain.models.ErrorType
import com.sample.ktorsample.core.domain.models.coins.CoinModel
import com.sample.ktorsample.core.domain.models.coins.CoinShortModel
import com.sample.ktorsample.feature.presentation.BinanceViewModel.Intent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BinanceScreen(
    viewModel: BinanceViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Binance — цены") })
        }
    ) { padding ->
        Box(Modifier
            .fillMaxSize()
            .padding(padding)) {

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
                            showDialog = true
                        }
                    )
            }

            if (showDialog && state.selectedSymbol != null) {
                TickerDialog(
                    symbol = state.selectedSymbol!!,
                    ticker = state.ticker,
                    isLoading = state.isLoadingTicker,
                    error = state.error,
                    onRetry = { viewModel.accept(Intent.RefreshTicker) },
                    onDismiss = {
                        showDialog = false
                        viewModel.accept(Intent.ClearError)
                    }
                )
            }
        }
    }
}

@Composable
private fun BinanceLoading(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
private fun BinanceError(
    modifier: Modifier,
    error: ErrorType,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    val message = remember(error) { error.toUiText() }
    Column(
        modifier = modifier
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onDismiss) { Text("Закрыть") }
            Button(onClick = onRetry) { Text("Повторить") }
        }
    }
}

private fun ErrorType.toUiText(): String = when (this) {
    ErrorType.NO_CONNECTION -> "Нет подключения к интернету"
    ErrorType.SERVER_ERROR -> "Ошибка сервера"
    ErrorType.UNKNOWN_ERROR -> "Неизвестная ошибка"
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

@Composable
private fun PriceRow(item: CoinShortModel, modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = item.symbol,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            text = item.price,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
private fun TickerDialog(
    symbol: String,
    ticker: CoinModel?,
    isLoading: Boolean,
    error: ErrorType?,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (error != null) {
                    TextButton(onClick = onRetry) { Text("Повторить") }
                }
                TextButton(onClick = onDismiss) { Text("Закрыть") }
            }
        },
        title = { Text(symbol) },
        text = {
            when {
                isLoading -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Text("Загружаю данные…")
                    }
                }

                error != null -> {
                    Text(error.toUiText())
                }

                ticker != null -> {
                    TickerBody(ticker)
                }

                else -> {
                    Text("Нет данных")
                }
            }
        }
    )
}

@Composable
private fun TickerBody(t: CoinModel) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        KeyValue("Последняя цена", t.lastPrice)
        KeyValue("Открытие (24ч)", t.openPrice)
        KeyValue("Макс (24ч)", t.highPrice)
        KeyValue("Мин (24ч)", t.lowPrice)
        KeyValue("Изм. (24ч)", "${t.priceChange} (${t.priceChangePercent}%)")
        KeyValue("Объём (24ч)", t.volume)
        KeyValue("Средневзвешенная (24ч)", t.weightedAvgPrice)
        KeyValue("Bid/Ask", "${t.bidPrice} / ${t.askPrice}")
    }
}

@Composable
private fun KeyValue(key: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            key,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(12.dp))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}
