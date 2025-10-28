package dev.calorai.mobile.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.calorai.mobile.main.features.home.HomeRoute
import dev.calorai.mobile.main.features.home.homeSection
import dev.calorai.mobile.main.features.plan.planSection
import dev.calorai.mobile.main.features.progress.progressSection
import dev.calorai.mobile.main.features.settings.settingsSection

@Composable
internal fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier,
    ) {
        homeSection()
        planSection()
        progressSection()
        settingsSection()
    }
}
