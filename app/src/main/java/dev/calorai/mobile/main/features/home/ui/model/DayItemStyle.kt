package dev.calorai.mobile.main.features.home.ui.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class DayItemStyle(
    val height: Dp,
    val width: Dp
) {
    SELECTED(
        height = 71.dp,
        width = 39.dp
    ),
    UNSELECTED(
        height = 52.dp,
        width = 31.dp
    ),
}