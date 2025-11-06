package com.sample.ktorsample.core.presentation.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sample.ktorsample.core.domain.models.ErrorType
import com.sample.ktorsample.core.presentation.utils.toUiText
import com.sample.ktorsample.ui.theme.KtorSampleTheme


@Composable
fun BinanceError(
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

@Preview(
    showBackground = true,
    device = Devices.PIXEL
)
@Composable
private fun PreviewBinanceError() {
    KtorSampleTheme {
        BinanceError(
            modifier = Modifier.fillMaxSize(),
            error = ErrorType.SERVER_ERROR,
            onRetry = {},
            onDismiss = {}
        )
    }
}
