package dev.calorai.mobile.features.main.ui

import dev.calorai.mobile.core.uikit.bottomNavBar.BottomNavItem
import dev.calorai.mobile.core.uikit.mealCard.MealType

sealed interface MainUiEvent {
    data object FetchStartDestination : MainUiEvent
    data class ModalMealButtonClick(val mealType: MealType) : MainUiEvent
    data class BottomNavItemSelect(val navItem: BottomNavItem) : MainUiEvent
    data object AddButtonClick : MainUiEvent
    data object BottomSheetHideRequest : MainUiEvent
}
