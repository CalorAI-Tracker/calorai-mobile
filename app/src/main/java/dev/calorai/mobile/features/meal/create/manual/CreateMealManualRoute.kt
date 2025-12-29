package dev.calorai.mobile.features.meal.create.manual

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.meal.create.manual.ui.CreateMealManualRoot
import dev.calorai.mobile.features.meal.domain.model.MealType
import kotlinx.serialization.Serializable

@Serializable
data class CreateMealManualRoute(
    val mealType: MealType,
    val date: String,
) : Destination

fun NavController.navigateToCreateMealManualScreen(
    mealType: MealType,
    date: String,
    navOptions: NavOptions? = null,
) {
    navigate(
        route = CreateMealManualRoute(
            mealType = mealType,
            date = date,
        ),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.createMealSection() {
    composable<CreateMealManualRoute> {
        CreateMealManualRoot()
    }
}
