package dev.calorai.mobile.core.uikit.pieChart

import androidx.annotation.StringRes

data class PieChartUiModel(
    val targetText: String,
    @StringRes val targetSubtext: Int,
    val leftText: String,
    val pieData: List<Float>
)
