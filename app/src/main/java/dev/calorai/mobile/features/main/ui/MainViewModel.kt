package dev.calorai.mobile.features.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavOptions
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.core.uikit.bottomNavBar.BottomNavItem
import dev.calorai.mobile.core.uikit.bottomNavBar.BottomNavItem.Companion.ITEMS
import dev.calorai.mobile.core.uikit.mealCard.MealType
import dev.calorai.mobile.features.main.features.home.navigateToHomeScreen
import dev.calorai.mobile.features.main.features.plan.navigateToPlanScreen
import dev.calorai.mobile.features.main.features.progress.navigateToProgressScreen
import dev.calorai.mobile.features.main.features.settings.navigateToSettingsScreen
import dev.calorai.mobile.features.meal.create.manual.navigateToCreateMealManualScreen
import dev.calorai.mobile.features.meal.details.navigateToMealDetailsScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel constructor(
    private val globalRouter: Router,
    private val mainRouter: Router,
) : ViewModel() {

    private val _state: MutableStateFlow<MainUiState> = MutableStateFlow(
        MainUiState(
            selectedItem = getSelectedItem(),
            bottomSheet = false,
        )
    )

    val state: StateFlow<MainUiState> = _state

    fun onEvent(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.ModalMealButtonClick -> navigateToCreateMealScreen(event.mealType)
            is MainUiEvent.BottomNavItemSelect -> handleBottomNavItemSelected(event.navItem)
            MainUiEvent.AddButtonClick -> _state.update { it.copy(bottomSheet = true) }
            MainUiEvent.BottomSheetHideRequest -> _state.update { it.copy(bottomSheet = false) }
            MainUiEvent.FetchStartDestination -> _state.update {
                it.copy(selectedItem = getSelectedItem())
            }
        }
    }

    private fun navigateToCreateMealScreen(mealType: MealType) {
        viewModelScope.launch {
            globalRouter.emit { navigateToMealDetailsScreen(mealType) }
        }
    }

    private fun handleBottomNavItemSelected(navItem: BottomNavItem) {
        viewModelScope.launch {
            _state.update { state -> state.copy(selectedItem = navItem) }
            mainRouter.emit {
                val options = NavOptions.Builder()
                    .setRestoreState(restoreState = true)
                    .setPopUpTo(
                        route = mainRouter.destination?.route,
                        inclusive = true,
                        saveState = true,
                    )
                    .build()
                when (navItem) {
                    BottomNavItem.Home -> navigateToHomeScreen(options)
                    BottomNavItem.Plan -> navigateToPlanScreen(options)
                    BottomNavItem.Progress -> navigateToProgressScreen(options)
                    BottomNavItem.Settings -> navigateToSettingsScreen(options)
                }
            }
        }
    }

    private fun getSelectedItem(): BottomNavItem = ITEMS.find { item ->
        mainRouter.destination?.hasRoute(item.route) == true
    } ?: BottomNavItem.Home
}
