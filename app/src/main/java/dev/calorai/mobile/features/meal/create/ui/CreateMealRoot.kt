package dev.calorai.mobile.features.meal.create.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.Pink
import dev.calorai.mobile.core.uikit.White
import dev.calorai.mobile.core.uikit.mealCard.MealType
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateMealRoot(
    viewModel: CreateMealViewModel = koinViewModel(),
) {
    CreateMealScreen(
        mealType = viewModel.mealType
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMealScreen(
    mealType: MealType,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Pink, White)), alpha = 0.3f),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "CreateMealScreen type = $mealType")
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateMealScreenPreview() {
    CalorAiTheme {
        CreateMealScreen(mealType = MealType.LUNCH)
    }
}
