package dev.calorai.mobile.features.meal.ready.ui

data class ReadyMealUi(
    val id: String,
    val title: String,
    val brand: String,
    val barcode: String,
    val summary: String,
)

sealed interface MealReadyListUiState {
    data object Loading : MealReadyListUiState
    data class Ready(
        val meals: List<ReadyMealUi>,
        val query: String = "",
        val selectedMealId: String? = null,
        val currentPage: Int = 0,
        val isAppending: Boolean = false,
        val canLoadMore: Boolean = false,
    ) : MealReadyListUiState
}
