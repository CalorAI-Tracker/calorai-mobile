package dev.calorai.mobile.features.meal.details.ui.model

import androidx.annotation.StringRes
import dev.calorai.mobile.R

enum class MacroLabelUi(@StringRes val labelResId: Int) {
    PROTEIN(R.string.details_meal_macro_label_protein),
    FAT(R.string.details_meal_macro_label_fat),
    CARBS(R.string.details_meal_macro_label_carbs),
}
