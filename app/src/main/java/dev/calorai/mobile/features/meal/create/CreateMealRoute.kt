package dev.calorai.mobile.features.meal.create

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.uikit.mealCard.MealType
import dev.calorai.mobile.features.meal.create.ui.CreateMealRoot
import kotlinx.serialization.Serializable

@Serializable
data class CreateMealRoute(val mealType: MealType)

fun NavController.navigateToCreateMealScreen(mealType: MealType, navOptions: NavOptions? = null) {
    navigate(route = CreateMealRoute(mealType), navOptions = navOptions)
}

fun NavGraphBuilder.createMealSection() {
    composable<CreateMealRoute> {
        CreateMealRoot()
    }
}
