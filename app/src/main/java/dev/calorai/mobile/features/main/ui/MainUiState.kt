package dev.calorai.mobile.features.main.ui

import dev.calorai.mobile.core.uikit.bottomNavBar.BottomNavItem

data class MainUiState(
    val selectedItem: BottomNavItem,
    val bottomSheet: Boolean,
)
