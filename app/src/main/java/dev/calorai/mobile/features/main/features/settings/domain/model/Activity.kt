package dev.calorai.mobile.features.main.features.settings.domain.model

import androidx.annotation.StringRes
import dev.calorai.mobile.R

enum class Activity(@StringRes val labelResId: Int) {
    LIGHT(R.string.settings_activity_2),
    MODERATE(R.string.settings_activity_4),
    ACTIVE(R.string.settings_activity_6),
    VERY_ACTIVE(R.string.settings_activity_7),
}