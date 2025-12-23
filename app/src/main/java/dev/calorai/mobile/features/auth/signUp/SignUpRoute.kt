package dev.calorai.mobile.features.auth.signUp

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.auth.signUp.ui.SignUpRoot
import kotlinx.serialization.Serializable

@Serializable
data object SignUpRoute : Destination

fun NavController.navigateToSignUpScreen(navOptions: NavOptions? = null) {
    navigate(route = SignUpRoute, navOptions = navOptions)
}

fun NavGraphBuilder.signUpSection() {
    composable<SignUpRoute> {
        SignUpRoot()
    }
}
