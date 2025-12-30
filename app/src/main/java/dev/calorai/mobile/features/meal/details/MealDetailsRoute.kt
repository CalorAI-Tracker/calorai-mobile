package dev.calorai.mobile.features.meal.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.meal.details.ui.MealDetailsRoot
import dev.calorai.mobile.features.meal.domain.model.MealType
import kotlinx.serialization.Serializable

@Serializable
data class MealDetailsRoute(
    val mealType: MealType,
    val date: String,
) : Destination

fun NavController.navigateToMealDetailsScreen(
    mealType: MealType,
    date: String,
    navOptions: NavOptions? = null
) {
    navigate(
        route = MealDetailsRoute(
            mealType = mealType,
            date = date,
        ), navOptions = navOptions
    )
}

fun NavGraphBuilder.mealDetailsSection() {
    composable<MealDetailsRoute> {
        MealDetailsRoot()
    }
}
