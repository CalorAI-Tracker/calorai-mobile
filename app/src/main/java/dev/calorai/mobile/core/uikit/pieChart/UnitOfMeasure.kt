package dev.calorai.mobile.core.uikit.pieChart

import androidx.annotation.StringRes
import dev.calorai.mobile.R

enum class UnitOfMeasure(@StringRes val unitResId: Int) {
    GRAM(R.string.pie_chart_unit_of_measure_gram),
    NONE(R.string.pie_chart_unit_of_measure_none),
}
