package dev.calorai.mobile.features.meal.ready.ui

data class ReadyMealUi(
    val id: Long,
    val title: String,
    val kcal: Int,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val quantityGrams: Double,
)

sealed interface MealReadyListUiState {
    data object Loading : MealReadyListUiState
    data class Ready(
        val meals: List<ReadyMealUi>,
        val query: String = "",
        val selectedMealId: Long? = null,
    ) : MealReadyListUiState
}
