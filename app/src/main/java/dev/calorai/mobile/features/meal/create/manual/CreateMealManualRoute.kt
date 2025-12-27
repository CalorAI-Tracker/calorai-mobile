package dev.calorai.mobile.features.meal.create.manual

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.meal.domain.model.MealType
import dev.calorai.mobile.features.meal.create.manual.ui.CreateMealManualRoot
import kotlinx.serialization.Serializable

@Serializable
data class CreateMealManualRoute(val mealId: Long) : Destination

fun NavController.navigateToCreateMealManualScreen(mealId: Long, navOptions: NavOptions? = null) {
    navigate(route = CreateMealManualRoute(mealId), navOptions = navOptions)
}

fun NavGraphBuilder.createMealSection() {
    composable<CreateMealManualRoute> {
        CreateMealManualRoot()
    }
}
