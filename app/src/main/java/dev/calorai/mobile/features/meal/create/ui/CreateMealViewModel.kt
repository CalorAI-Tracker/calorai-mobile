package dev.calorai.mobile.features.meal.create.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dev.calorai.mobile.features.meal.create.CreateMealRoute

class CreateMealViewModel constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mealRoute = savedStateHandle.toRoute<CreateMealRoute>()
    val mealType = mealRoute.mealType
}
