package dev.calorai.mobile.main.features.home.ui.views.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.times
import kotlin.math.PI

internal fun calculateItemOffset(
    itemSize: Dp,
    itemOverlapOffset: Dp,
    index: Int,
): Dp = -((index + 1) * (itemSize - itemOverlapOffset))

internal fun angleCorrectionCalc(
    strokeWidth: Float,
    outerRadius: Float,
    additionalOffsetCoeff: Float
): Float = (strokeWidth / (2 * outerRadius)) * (180f / PI.toFloat()) * additionalOffsetCoeff
