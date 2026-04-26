package dev.calorai.mobile.features.meal.ready

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.meal.domain.model.MealType
import dev.calorai.mobile.features.meal.ready.ui.MealReadyListRoot
import kotlinx.serialization.Serializable

@Serializable
data class MealReadyListRoute(
    val mealType: MealType,
    val date: String,
) : Destination

fun NavController.navigateToMealReadyListScreen(
    mealType: MealType,
    date: String,
    navOptions: NavOptions? = null,
) {
    navigate(
        route = MealReadyListRoute(
            mealType = mealType,
            date = date,
        ),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.mealReadyListSection() {
    composable<MealReadyListRoute> {
        MealReadyListRoot()
    }
}
