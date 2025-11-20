package dev.calorai.mobile.features.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.Pink
import dev.calorai.mobile.core.uikit.White
import dev.calorai.mobile.core.uikit.bottomNavBar.BottomNavBar
import dev.calorai.mobile.core.uikit.bottomNavBar.BottomNavItem
import dev.calorai.mobile.core.uikit.bottomNavBar.BottomNavItem.Companion.ITEMS
import dev.calorai.mobile.core.uikit.mealCard.MealType
import dev.calorai.mobile.features.main.MainNavGraph
import dev.calorai.mobile.features.main.features.home.navigateToHomeScreen
import dev.calorai.mobile.features.main.features.plan.navigateToPlanScreen
import dev.calorai.mobile.features.main.features.progress.navigateToProgressScreen
import dev.calorai.mobile.features.main.features.settings.navigateToSettingsScreen
import dev.calorai.mobile.features.meal.create.navigateToCreateMealScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainRoot(
    navController: NavHostController,
    viewModel: MainViewModel = koinViewModel(),
) {
    MainScreen(
        parentNavController = navController,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    parentNavController: NavHostController = rememberNavController(),
) {
    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = ITEMS.find { item ->
                    currentDestination?.hasRoute(item.route) == true
                } ?: BottomNavItem.Home,
                onItemSelected = { bottomNavItem ->
                    val options = NavOptions.Builder()
                        .setRestoreState(restoreState = true)
                        .setPopUpTo(
                            route = currentDestination?.route,
                            inclusive = true,
                            saveState = true,
                        )
                        .build()
                    when (bottomNavItem) {
                        BottomNavItem.Home -> mainNavController.navigateToHomeScreen(options)
                        BottomNavItem.Plan -> mainNavController.navigateToPlanScreen(options)
                        BottomNavItem.Progress -> mainNavController.navigateToProgressScreen(options)
                        BottomNavItem.Settings -> mainNavController.navigateToSettingsScreen(options)
                    }
                },
                onFabClick = { showBottomSheet = true },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(Pink, White)), alpha = 0.3f)
                .padding(top = innerPadding.calculateTopPadding(), bottom = 50.dp)
        ) {
            MainNavGraph(
                parentNavController = parentNavController,
                navController = mainNavController,
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                CreateMealBottomSheet(
                    navAction = { meal ->
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                            parentNavController.navigateToCreateMealScreen(meal)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CreateMealBottomSheet(
    navAction: (MealType) -> Unit = {},
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
        ModalButton("Breakfast") { navAction(MealType.BREAKFAST) }
        ModalButton("Lunch") { navAction(MealType.LUNCH) }
        ModalButton("Dinner") { navAction(MealType.DINNER) }
        ModalButton("Snack") { navAction(MealType.SNACK) }
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

@Preview
@Composable
private fun MainScreenPreview() {
    CalorAiTheme {
        MainScreen()
    }
}

@Preview
@Composable
private fun CreateMealBottomSheetPreview() {
    CalorAiTheme {
        CreateMealBottomSheet()
    }
}
