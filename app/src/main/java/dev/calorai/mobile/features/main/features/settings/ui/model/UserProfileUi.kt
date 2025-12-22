package dev.calorai.mobile.features.main.features.settings.ui.model

data class UserProfileUi(
    val name: String = "",
    val email: String = "",
    val birthDate: String = "",
    val gender: GenderUi? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val activity: ActivityUi? = null,
    val goal: GoalUi? = null,
)
