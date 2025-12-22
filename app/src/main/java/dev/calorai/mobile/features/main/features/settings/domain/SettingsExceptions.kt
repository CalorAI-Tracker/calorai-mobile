package dev.calorai.mobile.features.main.features.settings.domain

sealed class SettingsException(message: String? = null) : Exception(message) {
    class EmptyField : SettingsException()
    class NumberParseError : SettingsException()
    class BirthDateParseError : SettingsException()
    data class UnknownGender(val value: String) : SettingsException("Неизвестный пол: $value")
    data class UnknownActivity(val value: String) : SettingsException("Неизвестная активность: $value")
    data class UnknownGoal(val value: String) : SettingsException("Неизвестная цель: $value")
}
