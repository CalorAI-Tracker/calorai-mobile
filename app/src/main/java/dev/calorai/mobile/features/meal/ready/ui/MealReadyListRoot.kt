package dev.calorai.mobile.features.meal.ready.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel

private const val LOAD_MORE_THRESHOLD = 3
@Composable
fun MealReadyListRoot(
    viewModel: MealReadyListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler { viewModel.onEvent(MealReadyListUiEvent.BackClick) }
    MealReadyListScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun MealReadyListScreen(
    uiState: MealReadyListUiState,
    onEvent: (MealReadyListUiEvent) -> Unit,
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

            is MealReadyListUiState.Ready -> MealReadyListReadyContent(
                uiState = uiState,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun ColumnScope.MealReadyListReadyContent(
    uiState: MealReadyListUiState.Ready,
    onEvent: (MealReadyListUiEvent) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, uiState.meals.size, uiState.canLoadMore, uiState.isAppending) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .map { lastVisibleItemIndex ->
                val lastDataIndex = uiState.meals.lastIndex
                lastVisibleItemIndex != null &&
                    lastDataIndex >= 0 &&
                    lastVisibleItemIndex >= lastDataIndex - LOAD_MORE_THRESHOLD
            }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                onEvent(MealReadyListUiEvent.LoadNextPage)
            }
    }

    SearchField(
        value = uiState.query,
        onValueChange = { onEvent(MealReadyListUiEvent.QueryChange(it)) },
        placeholder = stringResource(R.string.meal_ready_search_placeholder),
    )

    Spacer(modifier = Modifier.height(16.dp))

    when {
        uiState.meals.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(
                        if (uiState.query.isBlank()) {
                            R.string.meal_ready_empty
                        } else {
                            R.string.meal_ready_nothing_found
                        }
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Medium,
                )
            }
        }

        else -> {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = uiState.meals,
                    key = ReadyMealUi::id,
                ) { meal ->
                    ReadyMealCard(
                        meal = meal,
                        isSelected = uiState.selectedMealId == meal.id,
                        onClick = { onEvent(MealReadyListUiEvent.MealClick(meal.id)) },
                    )
                }
                if (uiState.isAppending) {
                    item(key = "append_loader") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            PrimaryButton(
                onClick = { onEvent(MealReadyListUiEvent.AddClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                text = stringResource(R.string.add),
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
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
                text = meal.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            if (meal.brand.isNotBlank()) {
                Text(
                    text = meal.brand,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Text(
                text = meal.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
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
                        id = "1",
                        title = "Йогурт греческий 2%",
                        brand = "Teos",
                        barcode = "1234567890",
                        summary = "420 ккал, 24 г белка, 16.5 г жиров, 43 г углеводов на 100 г",
                    ),
                ),
                selectedMealId = "1",
            ),
            onEvent = {},
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
                        id = "1",
                        title = "Йогурт греческий 2%",
                        brand = "Teos",
                        barcode = "1234567890",
                        summary = "157 ккал, 17.5 г белка, 5 г жиров, 10.5 г углеводов на 100 г",
                    ),
                    ReadyMealUi(
                        id = "2",
                        title = "Йогурт греческий 5%",
                        brand = "Teos",
                        barcode = "1234567891",
                        summary = "187 ккал, 17.5 г белка, 10 г жиров, 10.5 г углеводов на 100 г",
                    ),
                    ReadyMealUi(
                        id = "3",
                        title = "Творог мягкий",
                        brand = "Савушкин",
                        barcode = "1234567892",
                        summary = "126 ккал, 14 г белка, 5 г жиров, 3 г углеводов на 100 г",
                    ),
                    ReadyMealUi(
                        id = "4",
                        title = "Овсяная каша",
                        brand = "",
                        barcode = "",
                        summary = "210 ккал, 6 г белка, 4.5 г жиров, 36 г углеводов на 100 г",
                    ),
                ),
                selectedMealId = "2",
                canLoadMore = true,
            ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MealReadyListScreenNothingFoundPreview() {
    CalorAiTheme {
        MealReadyListScreen(
            uiState = MealReadyListUiState.Ready(
                meals = emptyList(),
                query = "Авокадо",
            ),
            onEvent = {},
        )
    }
}
