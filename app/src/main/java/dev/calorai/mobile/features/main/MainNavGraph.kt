package dev.calorai.mobile.features.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.navigation.setupWith
import dev.calorai.mobile.features.home.HomeRoute
import dev.calorai.mobile.features.home.homeSection
import dev.calorai.mobile.features.main.ui.MainUiAction
import dev.calorai.mobile.features.plan.planSection
import dev.calorai.mobile.features.profile.profileSection
import dev.calorai.mobile.features.progress.progressSection
import kotlinx.coroutines.flow.SharedFlow
import org.koin.compose.koinInject
import org.koin.core.qualifier.qualifier

@Composable
internal fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    mainRouterController: RouterController = koinInject(qualifier<MainRouterContext>()),
    mainUiActions: SharedFlow<MainUiAction>,
) {
    mainRouterController.setupWith(navController, MainRouterContext)
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier,
    ) {
        homeSection(mainUiActions)
        planSection(mainUiActions)
        progressSection(mainUiActions)
        profileSection(mainUiActions)
    }
}
