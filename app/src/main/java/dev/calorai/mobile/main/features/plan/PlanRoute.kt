package dev.calorai.mobile.main.features.plan

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.main.features.plan.ui.PlanRoot
import kotlinx.serialization.Serializable

@Serializable
data object PlanRoute

fun NavController.navigateToPlanScreen(navOptions: NavOptions? = null) {
    navigate(route = PlanRoute, navOptions = navOptions)
}

fun NavGraphBuilder.planSection() {
    composable<PlanRoute> {
        PlanRoot()
    }
}
