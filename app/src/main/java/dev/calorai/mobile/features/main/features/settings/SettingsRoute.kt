package dev.calorai.mobile.features.main.features.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.features.main.features.settings.ui.SettingsRoot
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute

fun NavController.navigateToSettingsScreen(navOptions: NavOptions? = null) {
    navigate(route = SettingsRoute, navOptions = navOptions)
}

fun NavGraphBuilder.settingsSection() {
    composable<SettingsRoute> {
        SettingsRoot()
    }
}
