package dev.calorai.mobile.features.profile.data.dto.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ActivityCode {
    SEDENTARY,
    LIGHT,
    MODERATE,
    ACTIVE,
    VERY_ACTIVE,
}