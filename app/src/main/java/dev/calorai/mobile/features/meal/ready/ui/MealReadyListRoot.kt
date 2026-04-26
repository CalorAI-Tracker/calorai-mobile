package dev.calorai.mobile.features.meal.ready.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.PrimaryButton
import dev.calorai.mobile.core.uikit.commonGradientBackground
import org.koin.androidx.compose.koinViewModel

@Composable
fun MealReadyListRoot(
    viewModel: MealReadyListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(onBack = viewModel::onBackClick)
    MealReadyListScreen(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onMealClick = viewModel::onMealClick,
        onAddClick = viewModel::onAddClick,
    )
}

@Composable
private fun MealReadyListScreen(
    uiState: MealReadyListUiState,
    onQueryChange: (String) -> Unit,
    onMealClick: (Long) -> Unit,
    onAddClick: () -> Unit,
) {
    val system = WindowInsets.systemBars.asPaddingValues()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .commonGradientBackground()
            .padding(
                top = system.calculateTopPadding() + 32.dp,
                bottom = system.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp,
            ),
    ) {
        Text(
            text = stringResource(R.string.meal_ready_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (uiState) {
            MealReadyListUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

            is MealReadyListUiState.Ready -> {
                val filteredMeals = uiState.meals.filter { meal ->
                    uiState.query.isBlank() || meal.title.contains(uiState.query, ignoreCase = true)
                }

                SearchField(
                    value = uiState.query,
                    onValueChange = onQueryChange,
                    placeholder = stringResource(R.string.meal_ready_search_placeholder),
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.meals.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.meal_ready_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                } else if (filteredMeals.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.meal_ready_nothing_found),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(
                            items = filteredMeals,
                            key = ReadyMealUi::id,
                        ) { meal ->
                            ReadyMealCard(
                                meal = meal,
                                isSelected = uiState.selectedMealId == meal.id,
                                onClick = { onMealClick(meal.id) },
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    PrimaryButton(
                        onClick = onAddClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        text = stringResource(R.string.add),
                    )
                }

                if (uiState.meals.isNotEmpty() && filteredMeals.isEmpty().not()) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(32.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        ),
    )
}

@Composable
private fun ReadyMealCard(
    meal: ReadyMealUi,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(16.dp),
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(
                    R.string.meal_ready_title_with_weight,
                    meal.title,
                    formatNumber(meal.quantityGrams),
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = stringResource(
                    R.string.meal_ready_summary,
                    meal.kcal,
                    formatNumber(meal.protein),
                    formatNumber(meal.fat),
                    formatNumber(meal.carbs),
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

private fun formatNumber(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        value.toString().trimEnd('0').trimEnd('.')
    }
}

@Preview(showBackground = true)
@Composable
private fun MealReadyListScreenPreview() {
    CalorAiTheme {
        MealReadyListScreen(
            uiState = MealReadyListUiState.Ready(
                meals = listOf(
                    ReadyMealUi(
                        id = 1L,
                        title = "Йогурт греческий 2%",
                        kcal = 420,
                        protein = 24.0,
                        fat = 16.5,
                        carbs = 43.0,
                        quantityGrams = 250.0,
                    ),
                ),
                selectedMealId = 1L,
            ),
            onQueryChange = {},
            onMealClick = {},
            onAddClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MealReadyListScreenManyItemsPreview() {
    CalorAiTheme {
        MealReadyListScreen(
            uiState = MealReadyListUiState.Ready(
                meals = listOf(
                    ReadyMealUi(
                        id = 1L,
                        title = "Йогурт греческий 2%",
                        kcal = 157,
                        protein = 17.5,
                        fat = 5.0,
                        carbs = 10.5,
                        quantityGrams = 250.0,
                    ),
                    ReadyMealUi(
                        id = 2L,
                        title = "Йогурт греческий 5%",
                        kcal = 187,
                        protein = 17.5,
                        fat = 10.0,
                        carbs = 10.5,
                        quantityGrams = 250.0,
                    ),
                    ReadyMealUi(
                        id = 3L,
                        title = "Творог мягкий",
                        kcal = 126,
                        protein = 14.0,
                        fat = 5.0,
                        carbs = 3.0,
                        quantityGrams = 180.0,
                    ),
                    ReadyMealUi(
                        id = 4L,
                        title = "Овсяная каша",
                        kcal = 210,
                        protein = 6.0,
                        fat = 4.5,
                        carbs = 36.0,
                        quantityGrams = 200.0,
                    ),
                ),
                selectedMealId = 2L,
            ),
            onQueryChange = {},
            onMealClick = {},
            onAddClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MealReadyListScreenNothingFoundPreview() {
    CalorAiTheme {
        MealReadyListScreen(
            uiState = MealReadyListUiState.Ready(
                meals = listOf(
                    ReadyMealUi(
                        id = 1L,
                        title = "Йогурт греческий 2%",
                        kcal = 157,
                        protein = 17.5,
                        fat = 5.0,
                        carbs = 10.5,
                        quantityGrams = 250.0,
                    ),
                    ReadyMealUi(
                        id = 2L,
                        title = "Творог мягкий",
                        kcal = 126,
                        protein = 14.0,
                        fat = 5.0,
                        carbs = 3.0,
                        quantityGrams = 180.0,
                    ),
                ),
                query = "Авокадо",
            ),
            onQueryChange = {},
            onMealClick = {},
            onAddClick = {},
        )
    }
}
