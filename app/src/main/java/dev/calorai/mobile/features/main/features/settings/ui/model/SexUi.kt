package dev.calorai.mobile.features.main.features.settings.ui.model

import androidx.annotation.StringRes
import dev.calorai.mobile.R

enum class SexUi(@StringRes val labelResId: Int) {
    FEMALE(R.string.settings_sex_women),
    MALE(R.string.settings_sex_man),
}
