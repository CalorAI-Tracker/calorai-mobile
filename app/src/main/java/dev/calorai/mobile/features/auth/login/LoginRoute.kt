package dev.calorai.mobile.features.auth.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.auth.login.ui.LoginRoot
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute : Destination

fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
    navigate(route = LoginRoute, navOptions = navOptions)
}

fun NavGraphBuilder.loginSection() {
    composable<LoginRoute> {
        LoginRoot()
    }
}
