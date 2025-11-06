package com.sample.ktorsample.core.presentation.uikit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.sample.ktorsample.ui.theme.KtorSampleTheme

@Composable
fun BinanceLoading(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Preview(
    showBackground = true,
    device = Devices.PIXEL
)
@Composable
private fun PreviewBinanceLoading() {
    KtorSampleTheme {
        BinanceLoading(modifier = Modifier.fillMaxSize())
    }
}
