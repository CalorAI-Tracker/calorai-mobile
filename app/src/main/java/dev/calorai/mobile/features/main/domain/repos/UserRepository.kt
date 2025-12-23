package dev.calorai.mobile.features.main.domain.repos

import dev.calorai.mobile.features.main.data.api.UserProfileApi
import dev.calorai.mobile.features.main.data.dao.UserDao
import dev.calorai.mobile.features.main.data.dto.userProfile.createUser.CreateUserProfileRequest
import dev.calorai.mobile.features.main.data.dto.userProfile.updateUser.UpdateUserProfileRequest
import dev.calorai.mobile.features.main.data.mappers.toDomain
import dev.calorai.mobile.features.main.data.mappers.toEntity
import dev.calorai.mobile.features.main.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface UserRepository {

    suspend fun getUserProfile(userId: Int): User
    suspend fun updateUserProfile(request: UpdateUserProfileRequest)
    suspend fun createUserProfile(request: CreateUserProfileRequest)

    fun observeUser(): Flow<User>
}

class UserRepositoryImpl(
    private val api: UserProfileApi,
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun getUserProfile(userId: Int): User =
        withContext(dispatcher) {
            val local = userDao.getUser()
            if (local != null) {
                return@withContext local.toDomain()
            }
            val response = api.getUserProfile(userId)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            val body = response.body()
                ?: throw IllegalStateException(
                    "GetUserProfile: response successful but body is null"
                )
            val entity = body.toEntity()
            userDao.insert(entity)
            entity.toDomain()
        }

    override suspend fun updateUserProfile(request: UpdateUserProfileRequest) =
        withContext(dispatcher) {
            val userId = userDao.getUserId()
                ?: throw IllegalStateException("No userId found in DB")
            val response = api.updateUserProfile(userId, request)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            val body = response.body()
                ?: throw IllegalStateException(
                    "UpdateUserProfile: response successful but body is null"
                )
            userDao.update(body.toEntity())
        }

    override suspend fun createUserProfile(request: CreateUserProfileRequest) =
        withContext(dispatcher) {
            val response = api.createUserProfile(request)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            userDao.insert(request.toEntity())
        }

    override fun observeUser(): Flow<User> {
        return userDao
            .observeUser()
            .map { entity ->
                entity.toDomain()
            }
    }
}
