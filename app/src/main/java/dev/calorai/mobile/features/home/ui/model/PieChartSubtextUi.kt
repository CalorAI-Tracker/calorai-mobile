package dev.calorai.mobile.features.home.ui.model

import androidx.annotation.StringRes
import dev.calorai.mobile.R

enum class PieChartSubtextUi(@StringRes val labelResId: Int) {
    KCAL(R.string.home_piechart_subtext_kcal),
    PROTEIN(R.string.home_piechart_subtext_protein),
    FAT(R.string.home_piechart_subtext_fat),
    CARBS(R.string.home_piechart_subtext_carbs),
}
