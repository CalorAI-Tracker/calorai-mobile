package dev.calorai.mobile.features.meal.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.features.meal.details.ui.MealDetailsRoot
import kotlinx.serialization.Serializable

@Serializable
data class MealDetailsRoute(val mealId: Long)

fun NavController.navigateToMealDetailsScreen(mealId: Long, navOptions: NavOptions? = null) {
    navigate(route = MealDetailsRoute(mealId), navOptions = navOptions)
}

fun NavGraphBuilder.mealDetailsSection() {
    composable<MealDetailsRoute> {
        MealDetailsRoot()
    }
}
