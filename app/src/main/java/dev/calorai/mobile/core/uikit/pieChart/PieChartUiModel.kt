package dev.calorai.mobile.core.uikit.pieChart

data class PieChartUiModel(
    val targetText: String,
    val targetSubtext: String,
    val leftText: String,
    val pieData: List<Float>
)
