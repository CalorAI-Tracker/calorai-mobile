package dev.calorai.mobile.features.main.features.settings.data

import dev.calorai.mobile.features.main.features.settings.domain.SettingsRepository
import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserHealthProfilePayload
import dev.calorai.mobile.features.main.features.settings.domain.model.UserHealthProfile
import kotlinx.coroutines.delay

/**
 * Временная реализация, чтобы бизнес-логика сохранения работала без реального бэка.
 * Заменить на сетевую реализацию
 */
class FakeSettingsRepository : SettingsRepository {

    private val storage = mutableMapOf<Long, UserHealthProfile>()

    override suspend fun getUserHealthProfile(userId: Long): UserHealthProfile? {
        delay(100)
        return storage[userId]
    }

    override suspend fun updateUserHealthProfile(
        userId: Long,
        payload: UpdateUserHealthProfilePayload,
    ) {
        delay(300)
        storage[userId] = UserHealthProfile(
            userId = userId,
            sex = payload.sex,
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
