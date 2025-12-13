package dev.calorai.mobile.features.meal.create.manual

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.core.uikit.mealCard.MealType
import dev.calorai.mobile.features.meal.create.manual.ui.CreateMealManualRoot
import kotlinx.serialization.Serializable

@Serializable
data class CreateMealManualRoute(val mealType: MealType) : Destination

fun NavController.navigateToCreateMealManualScreen(mealType: MealType, navOptions: NavOptions? = null) {
    navigate(route = CreateMealManualRoute(mealType), navOptions = navOptions)
}

fun NavGraphBuilder.createMealSection() {
    composable<CreateMealManualRoute> {
        CreateMealManualRoot()
    }
}
