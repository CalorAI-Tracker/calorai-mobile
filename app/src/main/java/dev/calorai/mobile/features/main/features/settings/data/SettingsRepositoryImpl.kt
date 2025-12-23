package dev.calorai.mobile.features.main.features.settings.data

import dev.calorai.mobile.features.main.data.api.UserProfileApi
import dev.calorai.mobile.features.main.data.dao.UserDao
import dev.calorai.mobile.features.main.features.settings.domain.SettingsRepository
import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserProfilePayload
import dev.calorai.mobile.features.main.features.settings.domain.model.UserProfile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl constructor(
    private val userProfileApi: UserProfileApi,
    private val userDao: UserDao,
    private val mapper: SettingsMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : SettingsRepository {

    override suspend fun getUserProfile(userId: Long): UserProfile? = withContext(dispatcher) {
        try {
            val response = userProfileApi.getUserProfile(userId)
            if (!response.isSuccessful) {
                return@withContext getCachedUserProfile()
            }

            val userProfileResponse = response.body() ?: throw IllegalStateException()
            val profile = mapper.mapToDomain(userProfileResponse)
            userDao.update(mapper.mapToEntity(profile))
            return@withContext profile
        } catch (_: Exception) {
            return@withContext getCachedUserProfile()
        }
    }

    private suspend fun getCachedUserProfile(): UserProfile? {
        return userDao.observeUser().lastOrNull()?.let { mapper.mapToDomain(it) }
    }

    override suspend fun updateUserProfile(
        userId: Long,
        payload: UpdateUserProfilePayload,
    ) {
        withContext(dispatcher) {
            userDao.insert(mapper.mapToEntity(userId, payload))
            userProfileApi.updateUserProfile(
                userId = userId,
                body = mapper.mapToRequest(payload),
            )
        }
    }
}
