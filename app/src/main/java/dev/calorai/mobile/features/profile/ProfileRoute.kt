package dev.calorai.mobile.features.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.profile.ui.ProfileRoot
import kotlinx.serialization.Serializable

@Serializable
data object ProfileRoute : Destination

fun NavController.navigateToProfileScreen(navOptions: NavOptions? = null) {
    navigate(route = ProfileRoute, navOptions = navOptions)
}

fun NavGraphBuilder.profileSection() {
    composable<ProfileRoute> {
        ProfileRoot()
    }
}
