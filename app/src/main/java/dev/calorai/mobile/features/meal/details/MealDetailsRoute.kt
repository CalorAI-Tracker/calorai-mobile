package dev.calorai.mobile.features.meal.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.core.uikit.mealCard.MealType
import dev.calorai.mobile.features.meal.details.ui.MealDetailsRoot
import kotlinx.serialization.Serializable

@Serializable
data class MealDetailsRoute(val mealType: MealType) : Destination

fun NavController.navigateToMealDetailsScreen(mealType: MealType, navOptions: NavOptions? = null) {
    navigate(route = MealDetailsRoute(mealType), navOptions = navOptions)
}

fun NavGraphBuilder.mealDetailsSection() {
    composable<MealDetailsRoute> {
        MealDetailsRoot()
    }
}
