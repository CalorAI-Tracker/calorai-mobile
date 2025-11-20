package dev.calorai.mobile.features.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.calorai.mobile.features.main.features.home.HomeRoute
import dev.calorai.mobile.features.main.features.home.homeSection
import dev.calorai.mobile.features.main.features.plan.planSection
import dev.calorai.mobile.features.main.features.progress.progressSection
import dev.calorai.mobile.features.main.features.settings.settingsSection

@Composable
internal fun MainNavGraph(
    modifier: Modifier = Modifier,
    parentNavController: NavHostController,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier,
    ) {
        homeSection(parentNavController)
        planSection()
        progressSection()
        settingsSection()
    }
}
