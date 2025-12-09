package dev.calorai.mobile.features.main.features.settings.domain.model

import androidx.annotation.StringRes
import dev.calorai.mobile.R

enum class Goal(@StringRes val labelResId: Int) {
    LOSE_WEIGHT(R.string.settings_goal_weight_loss),
    KEEP_WEIGHT(R.string.settings_goal_weight_stability),
    GAIN_WEIGHT(R.string.settings_goal_get_weight),
}
