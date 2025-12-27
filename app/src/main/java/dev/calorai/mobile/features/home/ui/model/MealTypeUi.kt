package dev.calorai.mobile.features.home.ui.model

import androidx.annotation.StringRes
import dev.calorai.mobile.R

enum class MealTypeUi(@StringRes val labelResId: Int) {
    BREAKFAST(R.string.details_meal_type_breakfast),
    LUNCH(R.string.details_meal_type_lunch),
    DINNER(R.string.details_meal_type_dinner),
    SNACK(R.string.details_meal_type_snack),
}