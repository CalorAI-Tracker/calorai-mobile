package dev.calorai.mobile.features.profile.domain.error

sealed class ProfileException(message: String? = null) : Exception(message) {
    class EmptyField : ProfileException()
    class NumberParseError : ProfileException()
    class BirthDateParseError : ProfileException()
    data class UnknownGender(val value: String) : ProfileException("Неизвестный пол: $value")
    data class UnknownActivity(val value: String) : ProfileException("Неизвестная активность: $value")
    data class UnknownGoal(val value: String) : ProfileException("Неизвестная цель: $value")
}
