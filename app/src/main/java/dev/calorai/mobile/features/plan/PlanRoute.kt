package dev.calorai.mobile.features.plan

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.main.ui.MainUiAction
import dev.calorai.mobile.features.plan.ui.PlanRoot
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.Serializable

@Serializable
data object PlanRoute : Destination

fun NavController.navigateToPlanScreen(navOptions: NavOptions? = null) {
    navigate(route = PlanRoute, navOptions = navOptions)
}

fun NavGraphBuilder.planSection(
    mainUiActions: SharedFlow<MainUiAction>,
) {
    composable<PlanRoute> {
        PlanRoot(mainUiActions = mainUiActions)
    }
}
