package dev.calorai.mobile.features.meal.details.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.AddIngredientBottomPanel
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.PrimaryButton
import dev.calorai.mobile.core.uikit.commonGradientBackground
import dev.calorai.mobile.features.meal.domain.model.MealType
import dev.calorai.mobile.core.uikit.pieChart.PieChart
import dev.calorai.mobile.core.uikit.pieChart.PieChartStyle
import dev.calorai.mobile.core.uikit.pieChart.PieChartUiModel
import dev.calorai.mobile.features.meal.details.ui.model.MacroLabelUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun MealDetailsRoot(
    viewModel: MealDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    MealDetailsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}


@Composable
private fun MealDetailsScreen(
    uiState: MealDetailsUiState,
    onEvent: (MealDetailsUiEvent) -> Unit
) {
    val system = WindowInsets.systemBars.asPaddingValues()

    val title = when (uiState.mealType) {
        MealType.BREAKFAST -> stringResource(R.string.details_meal_type_breakfast)
        MealType.LUNCH -> stringResource(R.string.details_meal_type_lunch)
        MealType.DINNER -> stringResource(R.string.details_meal_type_dinner)
        MealType.SNACK -> stringResource(R.string.details_meal_type_snack)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .commonGradientBackground()
            .padding(
                top = system.calculateTopPadding(),
                bottom = system.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (!uiState.ingredients.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                uiState.macros.forEach { macro ->
                    PieChart(
                        pieChartData = PieChartUiModel(
                            targetText = macro.valueText,
                            targetSubtext = macro.label,
                            leftText = "",
                            pieData = macro.values
                        ),
                        configuration = PieChartStyle.MEDIUM
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.details_ingredients),
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = stringResource(R.string.details_add_more),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable {
                        onEvent(MealDetailsUiEvent.AddMoreClick)
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.ingredients) { ingredient ->
                    IngredientItem(
                        title = ingredient.title,
                        kcal = ingredient.kcal,
                        weight = ingredient.weight,
                        onClick = {
                            onEvent(MealDetailsUiEvent.IngredientClick(ingredient))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                onClick = {
                    onEvent(MealDetailsUiEvent.ContinueClick)
                },
                text = stringResource(R.string.details_done)
            )
        }
    }

    if (uiState.showAddIngredientSheet || uiState.ingredients.isEmpty()) {
        AddIngredientBottomPanel(
            onDismiss = {
                if (!uiState.ingredients.isEmpty())
                    onEvent(MealDetailsUiEvent.CloseAddIngredient)
            },
            onAddManualClick = {
                onEvent(MealDetailsUiEvent.AddManualClick)
            },
            onChooseReadyClick = {
                onEvent(MealDetailsUiEvent.ChooseReadyClick)
            }
        )
    }
}

@Composable
private fun IngredientItem(
    title: String,
    kcal: String,
    weight: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(text = " • $kcal", color = MaterialTheme.colorScheme.onSurface)
            }

            Text(
                text = weight,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MealDetailsScreenPreview() {
    CalorAiTheme {
        MealDetailsScreen(
            uiState = MealDetailsUiState(
                mealType = MealType.DINNER,
                macros = listOf(
                    MacroUi("16 г", MacroLabelUi.PROTEIN.labelResId, listOf(70f, 30f)),
                    MacroUi("24 г", MacroLabelUi.FAT.labelResId, listOf(85f, 15f)),
                    MacroUi("14 г", MacroLabelUi.CARBS.labelResId, listOf(60f, 40f)),
                ),
                ingredients = listOf(
//                    IngredientUi("Креветки", "99 ккал", "8 шт"),
//                    IngredientUi("Рис", "130 ккал", "100 г"),
//                    IngredientUi("Яйцо пашот", "68 ккал", "1 шт"),
//                    IngredientUi("Помидоры черри", "9 ккал", "3 шт"),
//                    IngredientUi("Авокадо", "0 ккал", "70 г"),
                )
            ),
            onEvent = {}
        )
    }
}
