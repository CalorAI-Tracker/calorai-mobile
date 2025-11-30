package dev.calorai.mobile.features.meal.details.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.commonGradientBackground
import org.koin.androidx.compose.koinViewModel

@Composable
fun MealDetailsRoot(
    viewModel: MealDetailsViewModel = koinViewModel(),
) {
    MealDetailsScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealDetailsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .commonGradientBackground(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "MealDetailsScreen")
    }
}

@Preview(showBackground = true)
@Composable
private fun MealDetailsScreenPreview() {
    CalorAiTheme {
        MealDetailsScreen()
    }
}
