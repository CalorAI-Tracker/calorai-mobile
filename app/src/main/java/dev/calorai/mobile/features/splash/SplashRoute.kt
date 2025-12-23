package dev.calorai.mobile.features.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.splash.ui.SplashRoot
import kotlinx.serialization.Serializable

@Serializable
data object SplashRoute : Destination

fun NavGraphBuilder.splashSection() {
    composable<SplashRoute> {
        SplashRoot()
    }
}
