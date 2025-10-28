package dev.calorai.mobile.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.auth.ui.AuthRoot
import dev.calorai.mobile.main.ui.MainRoot
import kotlinx.serialization.Serializable

@Serializable
data object AuthRoute


fun NavController.navigateToAuthScreen(navOptions: NavOptions? = null) {
    navigate(route = AuthRoute, navOptions = navOptions)
}

fun NavGraphBuilder.authSection(
    navigateToAuthorizedZone: () -> Unit,
) {
    composable<AuthRoute> {
        AuthRoot(
            navigateToAuthorizedZone = navigateToAuthorizedZone,
        )
    }
}
