package dev.calorai.mobile.features.meal.ready

import androidx.annotation.Keep
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.meal.domain.model.MealType
import dev.calorai.mobile.features.meal.ready.ui.MealReadyListRoot
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class MealReadyListSource {
    HOME,
    DETAILS,
}

@Serializable
data class MealReadyListRoute(
    val mealType: MealType,
    val date: String,
    val source: MealReadyListSource,
) : Destination

fun NavController.navigateToMealReadyListScreen(
    mealType: MealType,
    date: String,
    source: MealReadyListSource,
    navOptions: NavOptions? = null,
) {
    navigate(
        route = MealReadyListRoute(
            mealType = mealType,
            date = date,
            source = source,
        ),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.mealReadyListSection() {
    composable<MealReadyListRoute> {
        MealReadyListRoot()
    }
}
