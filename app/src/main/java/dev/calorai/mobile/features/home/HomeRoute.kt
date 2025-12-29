package dev.calorai.mobile.features.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.home.ui.HomeRoot
import dev.calorai.mobile.features.main.ui.MainUiAction
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute : Destination

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) {
    navigate(route = HomeRoute, navOptions = navOptions)
}

fun NavGraphBuilder.homeSection(
    mainUiActions: SharedFlow<MainUiAction>,
) {
    composable<HomeRoute> {
        HomeRoot(mainUiActions = mainUiActions)
    }
}
