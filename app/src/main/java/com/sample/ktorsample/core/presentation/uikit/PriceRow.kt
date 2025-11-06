package com.sample.ktorsample.core.presentation.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sample.ktorsample.core.domain.models.coins.CoinShortModel
import com.sample.ktorsample.ui.theme.KtorSampleTheme

@Composable
fun PriceRow(item: CoinShortModel, modifier: Modifier = Modifier) {
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

@Preview(
    showBackground = true,
    device = Devices.PIXEL
)
@Composable
private fun Preview_PriceRow() {
    val coin1 = CoinShortModel(symbol = "BTCUSDT", price = "104210.50")
    val coin2 = CoinShortModel(symbol = "ETHUSDT", price = "3012.00034")

    KtorSampleTheme {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            PriceRow(item = coin1, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            PriceRow(item = coin2, modifier = Modifier.fillMaxWidth())
        }
    }
}
