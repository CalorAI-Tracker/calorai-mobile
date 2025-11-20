package dev.calorai.mobile.core.uikit.pieChart

import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class PieChartStyle(
    val chartSize: Dp,
    val innerRatio: Float,
    val additionalOffsetCoeff: Float,
    val labelStyle: @Composable () -> TextStyle,
    val textStyle: @Composable () -> TextStyle
) {
    LARGE(
        chartSize = 277.dp,
        innerRatio = 0.65f,
        additionalOffsetCoeff = 1.3f,
        labelStyle = { typography.displayLarge },
        textStyle = { typography.bodyLarge }
    ),
    MEDIUM(
        chartSize = 117.dp,
        innerRatio = 0.736f,
        additionalOffsetCoeff = 1.25f,
        labelStyle = { typography.bodyLarge },
        textStyle = { typography.bodySmall }
    ),
}