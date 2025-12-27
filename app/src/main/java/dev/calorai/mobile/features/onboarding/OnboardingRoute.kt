package dev.calorai.mobile.features.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.onboarding.ui.OnboardingRoot
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingRoute(val name: String) : Destination

fun NavController.navigateToOnboardingScreen(name: String, navOptions: NavOptions? = null) {
    navigate(route = OnboardingRoute(name), navOptions = navOptions)
}

fun NavGraphBuilder.onboardingSection() {
    composable<OnboardingRoute> {
        OnboardingRoot()
    }
}
