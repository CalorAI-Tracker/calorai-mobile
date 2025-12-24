package dev.calorai.mobile.features.profile.data

import dev.calorai.mobile.features.profile.data.api.UserProfileApi
import dev.calorai.mobile.features.profile.data.dao.UserDao
import dev.calorai.mobile.features.profile.domain.ProfileRepository
import dev.calorai.mobile.features.profile.domain.model.CreateUserProfilePayload
import dev.calorai.mobile.features.profile.domain.model.UpdateUserProfilePayload
import dev.calorai.mobile.features.profile.domain.model.UserId
import dev.calorai.mobile.features.profile.domain.model.UserProfile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProfileRepositoryImpl constructor(
    private val userProfileApi: UserProfileApi,
    private val userDao: UserDao,
    private val mapper: ProfileMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ProfileRepository {

    override suspend fun getUserProfile(userId: UserId): UserProfile? = withContext(dispatcher) {
        try {
            val response = userProfileApi.getUserProfile(userId.value)
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
        userId: UserId,
        payload: UpdateUserProfilePayload,
    ) {
        withContext(dispatcher) {
            userDao.insert(mapper.mapToEntity(userId, payload))
            userProfileApi.updateUserProfile(
                userId = userId.value,
                body = mapper.mapToRequest(payload),
            )
        }
    }

    override suspend fun createUserProfile(payload: CreateUserProfilePayload): UserId =
        withContext(dispatcher) {
            val response = userProfileApi.createUserProfile(mapper.mapToRequest(payload))
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            userDao.insert(mapper.mapToEntity(payload))
            payload.userId
        }
}
