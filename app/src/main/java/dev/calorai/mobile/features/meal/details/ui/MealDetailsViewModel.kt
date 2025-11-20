package dev.calorai.mobile.features.meal.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dev.calorai.mobile.features.meal.details.MealDetailsRoute

class MealDetailsViewModel constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mealRoute = savedStateHandle.toRoute<MealDetailsRoute>()
    val mealId = mealRoute.mealId
}
