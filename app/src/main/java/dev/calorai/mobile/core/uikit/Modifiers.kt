package dev.calorai.mobile.core.uikit

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

fun Modifier.commonGradientBackground(): Modifier =
    this.background(
        Brush.verticalGradient(
            colors = listOf(Pink, White),
        ),
        alpha = 0.8f
    )