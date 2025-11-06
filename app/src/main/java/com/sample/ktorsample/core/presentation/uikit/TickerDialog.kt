package com.sample.ktorsample.core.presentation.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sample.ktorsample.core.domain.models.ErrorType
import com.sample.ktorsample.core.domain.models.coins.CoinModel
import com.sample.ktorsample.core.presentation.utils.toUiText
import com.sample.ktorsample.ui.theme.KtorSampleTheme

@Composable
fun TickerDialog(
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
private fun TickerBody(
    t: CoinModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
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
private fun KeyValue(
    key: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            key,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(12.dp))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Preview(
    name = "TickerDialog — Error",
    showBackground = true,
    device = Devices.PIXEL
)
@Composable
private fun PreviewTickerDialogError() {
    KtorSampleTheme {
        TickerDialog(
            symbol = "BTCUSDT",
            ticker = null,
            isLoading = false,
            error = ErrorType.SERVER_ERROR,
            onRetry = {},
            onDismiss = {}
        )
    }
}

@Preview(
    name = "TickerDialog — Loading",
    showBackground = true,
    device = Devices.PIXEL
)
@Composable
private fun PreviewTickerDialogLoading() {
    KtorSampleTheme {
        TickerDialog(
            symbol = "BTCUSDT",
            ticker = null,
            isLoading = true,
            error = null,
            onRetry = {},
            onDismiss = {}
        )
    }
}

@Preview(
    name = "TickerDialog — Success",
    showBackground = true,
    device = Devices.PIXEL
)
@Composable
private fun PreviewTickerDialogSuccess() {
    val coin = CoinModel(
        askPrice = "64000.00",
        askQty = "1.2",
        bidPrice = "63990.00",
        bidQty = "0.8",
        closeTime = 0L,
        count = 1234,
        firstId = 100000L,
        highPrice = "65000.00",
        lastId = 100999L,
        lastPrice = "64210.50",
        lastQty = "0.01",
        lowPrice = "63000.00",
        openPrice = "63500.00",
        openTime = 0L,
        prevClosePrice = "63490.00",
        priceChange = "710.50",
        priceChangePercent = "1.12",
        quoteVolume = "123456789.0",
        symbol = "BTCUSDT",
        volume = "56789.0",
        weightedAvgPrice = "64100.00"
    )

    KtorSampleTheme {
        TickerDialog(
            symbol = "BTCUSDT",
            ticker = coin,
            isLoading = false,
            error = null,
            onRetry = {},
            onDismiss = {}
        )
    }
}
