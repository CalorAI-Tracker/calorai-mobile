package dev.calorai.mobile.features.main.features.settings.data

import dev.calorai.mobile.features.main.features.settings.domain.SettingsRepository
import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserProfilePayload
import dev.calorai.mobile.features.main.features.settings.domain.model.UserProfile
import kotlinx.coroutines.delay

/**
 * Временная реализация, чтобы бизнес-логика сохранения работала без реального бэка.
 * Заменить на сетевую реализацию
 */
class FakeSettingsRepository : SettingsRepository {

    private val storage = mutableMapOf<Long, UserProfile>()

    override suspend fun getUserProfile(userId: Long): UserProfile? {
        delay(100)
        return storage[userId]
    }

    override suspend fun updateUserProfile(
        userId: Long,
        payload: UpdateUserProfilePayload,
    ) {
        delay(300)
        storage[userId] = UserProfile(
            userId = userId,
            name = payload.name,
            email = payload.email,
            gender = payload.gender,
            height = payload.height,
            weight = payload.weight,
            birthDay = payload.birthDay,
            activityCode = payload.activityCode,
            healthGoalCode = payload.healthGoalCode,
            targetWeightKg = payload.targetWeightKg,
            weeklyRateKg = payload.weeklyRateKg,
        )
    }
}
