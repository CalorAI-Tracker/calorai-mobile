package dev.calorai.mobile.main.features.home.ui.model

data class PieChartUiModel(
    val targetText: String,
    val targetSubtext: String,
    val leftText: String,
    val pieData: List<Float>
)
