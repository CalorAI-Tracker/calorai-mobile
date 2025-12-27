package dev.calorai.mobile.features.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.bottomNavBar.BottomNavBar
import dev.calorai.mobile.core.uikit.bottomNavBar.BottomNavItem
import dev.calorai.mobile.core.uikit.commonGradientBackground
import dev.calorai.mobile.features.meal.domain.model.MealType
import dev.calorai.mobile.features.main.MainNavGraph
import dev.calorai.mobile.features.home.ui.HomeScreenPreview
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainRoot(
    viewModel: MainViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MainScreen(
        state = state,
        onEvent = viewModel::onEvent,
        content = {
            MainNavGraph()
            LaunchedEffect(MainUiEvent.FetchStartDestination) {
                viewModel.onEvent(MainUiEvent.FetchStartDestination)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    state: MainUiState,
    onEvent: (MainUiEvent) -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = state.selectedItem,
                onItemSelected = { onEvent(MainUiEvent.BottomNavItemSelect(it)) },
                onFabClick = { onEvent(MainUiEvent.AddButtonClick) },
                bottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding(),
            )
        },
        containerColor = Color.Unspecified,
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .commonGradientBackground()
                .padding(top = innerPadding.calculateTopPadding(), bottom = 50.dp)
        ) {
            content()
        }
        if (state.bottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { onEvent(MainUiEvent.BottomSheetHideRequest) },
                sheetState = sheetState,
            ) {
                CreateMealBottomSheet(
                    onMealButtonClick = { meal ->
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) onEvent(MainUiEvent.BottomSheetHideRequest)
                            onEvent(MainUiEvent.ModalMealButtonClick(meal))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CreateMealBottomSheet(
    onMealButtonClick: (MealType) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.select_meal_type),
            style = MaterialTheme.typography.titleLarge
        )
        ModalButton("Breakfast") { onMealButtonClick(MealType.BREAKFAST) }
        ModalButton("Lunch") { onMealButtonClick(MealType.LUNCH) }
        ModalButton("Dinner") { onMealButtonClick(MealType.DINNER) }
        ModalButton("Snack") { onMealButtonClick(MealType.SNACK) }
    }
}

@Composable
private fun ModalButton(
    text: String,
    onClick: () -> Unit = {},
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
        onClick = onClick,
    ) {
        Text(
            text, color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    CalorAiTheme {
        MainScreen(
            state = MainUiState(
                selectedItem = BottomNavItem.Home,
                bottomSheet = false,
            ),
            content = { HomeScreenPreview() }
        )
    }
}

@Preview
@Composable
private fun CreateMealBottomSheetPreview() {
    CalorAiTheme {
        CreateMealBottomSheet()
    }
}
