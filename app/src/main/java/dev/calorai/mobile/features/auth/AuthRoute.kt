package dev.calorai.mobile.features.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.auth.ui.AuthRoot
import dev.calorai.mobile.core.navigation.Destination
import kotlinx.serialization.Serializable

@Serializable
data object AuthRoute : Destination

fun NavController.navigateToAuthScreen(navOptions: NavOptions? = null) {
    navigate(route = AuthRoute, navOptions = navOptions)
}

fun NavGraphBuilder.authSection() {
    composable<AuthRoute> {
        AuthRoot()
    }
}
