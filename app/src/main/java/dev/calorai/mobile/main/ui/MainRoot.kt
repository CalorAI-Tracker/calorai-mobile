package dev.calorai.mobile.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.calorai.mobile.main.ui.bottomNavBar.BottomNavBar
import dev.calorai.mobile.main.ui.bottomNavBar.BottomNavItem
import dev.calorai.mobile.main.ui.bottomNavBar.BottomNavItem.Companion.ITEMS
import dev.calorai.mobile.main.MainNavGraph
import dev.calorai.mobile.main.features.home.navigateToHomeScreen
import dev.calorai.mobile.main.features.plan.navigateToPlanScreen
import dev.calorai.mobile.main.features.progress.navigateToProgressScreen
import dev.calorai.mobile.main.features.settings.navigateToSettingsScreen
import dev.calorai.mobile.ui.theme.CalorAiTheme
import dev.calorai.mobile.ui.theme.Pink
import dev.calorai.mobile.ui.theme.White

@Composable
fun MainRoot() {
    MainScreen()
}

@Composable
private fun MainScreen() {
    val mainNavController = rememberNavController()
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = ITEMS.find { item ->
                    currentDestination?.hasRoute(item.route) == true
                } ?: BottomNavItem.Home,
                onItemSelected = { bottomNavItem ->
                    val options = NavOptions.Builder()
                        .setPopUpTo(currentDestination?.route, true)
                        .build()
                    when (bottomNavItem) {
                        BottomNavItem.Home -> mainNavController.navigateToHomeScreen(options)
                        BottomNavItem.Plan -> mainNavController.navigateToPlanScreen(options)
                        BottomNavItem.Progress -> mainNavController.navigateToProgressScreen(options)
                        BottomNavItem.Settings -> mainNavController.navigateToSettingsScreen(options)
                    }
                },
                onFabClick = {},
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(Pink, White)))
                .padding(innerPadding)
        ) {
            MainNavGraph(
                navController = mainNavController,
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    CalorAiTheme {
        MainScreen()
    }
}
