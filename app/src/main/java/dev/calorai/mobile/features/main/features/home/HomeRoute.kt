package dev.calorai.mobile.features.main.features.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.features.main.features.home.ui.HomeRoot
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) {
    navigate(route = HomeRoute, navOptions = navOptions)
}

fun NavGraphBuilder.homeSection(
    parentNavController: NavHostController,
) {
    composable<HomeRoute> {
        HomeRoot(parentNavController)
    }
}
