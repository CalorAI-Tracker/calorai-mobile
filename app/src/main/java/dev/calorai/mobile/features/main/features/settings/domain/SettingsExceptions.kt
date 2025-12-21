package dev.calorai.mobile.features.main.features.settings.domain

sealed class SettingsException(message: String) : Exception(message) {
    data class NumberParseError(val value: String) : SettingsException("Не удалось прочитать число из \"$value\"")
    data class UnknownGender(val value: String) : SettingsException("Неизвестный пол: $value")
    data class UnknownActivity(val value: String) : SettingsException("Неизвестная активность: $value")
    data class UnknownGoal(val value: String) : SettingsException("Неизвестная цель: $value")
}
