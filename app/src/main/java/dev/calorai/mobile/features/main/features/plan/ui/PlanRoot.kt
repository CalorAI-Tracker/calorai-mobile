package dev.calorai.mobile.features.main.features.plan.ui

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
fun PlanRoot(
    viewModel: PlanViewModel = koinViewModel(),
) {
    PlanScreen()
}

@Composable
private fun PlanScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "PlanScreen")
    }
}

@Preview(showBackground = true)
@Composable
private fun PlanScreenPreview() {
    CalorAiTheme {
        PlanScreen()
    }
}
