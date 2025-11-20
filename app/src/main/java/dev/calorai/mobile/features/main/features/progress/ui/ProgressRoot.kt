package dev.calorai.mobile.features.main.features.progress.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.calorai.mobile.core.uikit.CalorAiTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProgressRoot(
    viewModel: ProgressViewModel = koinViewModel(),
) {
    ProgressScreen()
}

@Composable
private fun ProgressScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "ProgressScreen")
    }
}

@Preview(showBackground = true)
@Composable
private fun ProgressScreenPreview() {
    CalorAiTheme {
        ProgressScreen()
    }
}
