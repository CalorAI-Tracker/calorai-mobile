package dev.calorai.mobile.mainScreen.ui.model

data class PieChartUiModel(
    val targetText: String,
    val targetSubtext: String,
    val leftText: String,
    val pieData: List<Float> // String - число + единицы; Float - доля от общего числа
)
