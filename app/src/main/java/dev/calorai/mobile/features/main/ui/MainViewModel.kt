package dev.calorai.mobile.features.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavOptions
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.core.uikit.bottomNavBar.BottomNavItem
import dev.calorai.mobile.core.uikit.bottomNavBar.BottomNavItem.Companion.ITEMS
import dev.calorai.mobile.features.home.navigateToHomeScreen
import dev.calorai.mobile.features.plan.navigateToPlanScreen
import dev.calorai.mobile.features.profile.navigateToProfileScreen
import dev.calorai.mobile.features.progress.navigateToProgressScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel constructor(
    private val mainRouter: Router,
) : ViewModel() {

    private val _state: MutableStateFlow<MainUiState> = MutableStateFlow(
        MainUiState(
            selectedItem = getSelectedItem(),
            bottomSheet = false,
        )
    )

    val state: StateFlow<MainUiState> = _state

    private val _uiActions = MutableSharedFlow<MainUiAction>()
    val uiActions: SharedFlow<MainUiAction> = _uiActions.asSharedFlow()

    fun onEvent(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.ModalMealButtonClick -> {
                viewModelScope.launch {
                    _uiActions.emit(MainUiAction.ModalCreateMealButtonClick(event.mealType))
                }
            }

            is MainUiEvent.BottomNavItemSelect -> handleBottomNavItemSelected(event.navItem)
            MainUiEvent.AddButtonClick -> _state.update { it.copy(bottomSheet = true) }
            MainUiEvent.BottomSheetHideRequest -> _state.update { it.copy(bottomSheet = false) }
            MainUiEvent.FetchStartDestination -> _state.update {
                it.copy(selectedItem = getSelectedItem())
            }
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
                    BottomNavItem.Profile -> navigateToProfileScreen(options)
                }
            }
        }
    }

    private fun getSelectedItem(): BottomNavItem = ITEMS.find { item ->
        mainRouter.destination?.hasRoute(item.route) == true
    } ?: BottomNavItem.Home
}
