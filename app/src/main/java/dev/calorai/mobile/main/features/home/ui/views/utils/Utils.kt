package dev.calorai.mobile.main.features.home.ui.views.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times

internal fun calculateItemOffset(
    itemSize: Dp,
    itemOverlapOffset: Dp,
    index: Int,
): Dp = -((index + 1) * (itemSize - itemOverlapOffset))
