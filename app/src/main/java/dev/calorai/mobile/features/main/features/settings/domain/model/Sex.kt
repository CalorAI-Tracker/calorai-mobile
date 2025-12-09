package dev.calorai.mobile.features.main.features.settings.domain.model

import androidx.annotation.StringRes
import dev.calorai.mobile.R

enum class Sex(@StringRes val labelResId: Int) {
    FEMALE(R.string.settings_sex_women),
    MALE(R.string.settings_sex_man),
}


