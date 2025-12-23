package dev.calorai.mobile.features.profile.ui.model

import androidx.annotation.StringRes
import dev.calorai.mobile.R

enum class GoalUi(@StringRes val labelResId: Int) {
    LOSE_WEIGHT(R.string.settings_goal_weight_loss),
    KEEP_WEIGHT(R.string.settings_goal_weight_stability),
    GAIN_WEIGHT(R.string.settings_goal_get_weight),
}
