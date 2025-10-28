package dev.calorai.mobile.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.main.ui.MainRoot
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

fun NavController.navigateToMainScreen(navOptions: NavOptions? = null) {
    navigate(route = MainRoute, navOptions = navOptions)
}

fun NavGraphBuilder.mainSection() {
    composable<MainRoute> {
        MainRoot()
    }
}
