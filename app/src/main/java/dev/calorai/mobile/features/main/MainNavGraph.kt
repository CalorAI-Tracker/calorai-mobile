package dev.calorai.mobile.features.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.navigation.setupWith
import dev.calorai.mobile.features.main.features.home.HomeRoute
import dev.calorai.mobile.features.main.features.home.homeSection
import dev.calorai.mobile.features.main.features.plan.planSection
import dev.calorai.mobile.features.main.features.progress.progressSection
import dev.calorai.mobile.features.main.features.settings.settingsSection
import org.koin.compose.koinInject
import org.koin.core.qualifier.qualifier

@Composable
internal fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    mainRouterController: RouterController = koinInject(qualifier<MainRouterContext>()),
) {
    mainRouterController.setupWith(navController, MainRouterContext)
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
