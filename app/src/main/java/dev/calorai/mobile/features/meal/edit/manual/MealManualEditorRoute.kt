package dev.calorai.mobile.features.meal.edit.manual

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dev.calorai.mobile.core.navigation.Destination
import dev.calorai.mobile.features.meal.domain.model.MealType
import dev.calorai.mobile.features.meal.edit.manual.ui.MealManualEditorRoot
import kotlinx.serialization.Serializable

@Serializable
data class MealManualEditorRoute(
    val mealType: MealType,
    val date: String,
    val entryId: Long? = null,
) : Destination

fun NavController.navigateToMealManualEditorScreen(
    mealType: MealType,
    date: String,
    entryId: Long? = null,
    navOptions: NavOptions? = null,
) {
    navigate(
        route = MealManualEditorRoute(
            mealType = mealType,
            date = date,
            entryId = entryId,
        ),
        navOptions = navOptions
    )
}

fun NavGraphBuilder.mealEditorSection() {
    composable<MealManualEditorRoute> {
        MealManualEditorRoot()
    }
}
