package dev.calorai.mobile.features.profile.ui.model

import androidx.annotation.StringRes
import dev.calorai.mobile.R

enum class GoalUi(@StringRes val labelResId: Int) {
    LOSE_WEIGHT(R.string.settings_goal_lose_weight),
    GAIN_WEIGHT(R.string.settings_goal_gain_weight),
    GAIN_MUSCLE(R.string.settings_goal_gain_muscle),
    MAINTAIN(R.string.settings_goal_maintain),
}
