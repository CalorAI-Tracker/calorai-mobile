package dev.calorai.mobile.features.progress

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.progress.ui.ProgressRoot
import kotlinx.serialization.Serializable

@Serializable
data object ProgressRoute : Destination

fun NavController.navigateToProgressScreen(navOptions: NavOptions? = null) {
    navigate(route = ProgressRoute, navOptions = navOptions)
}

fun NavGraphBuilder.progressSection() {
    composable<ProgressRoute> {
        ProgressRoot()
    }
}
