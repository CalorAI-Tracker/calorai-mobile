package dev.calorai.mobile.features.main.domain.repos

import dev.calorai.mobile.features.main.data.api.UserProfileApi
import dev.calorai.mobile.features.main.data.dao.UserDao
import dev.calorai.mobile.features.main.data.dto.userProfile.createUser.CreateUserProfileRequest
import dev.calorai.mobile.features.main.data.dto.userProfile.getUser.GetUserProfileResponse
import dev.calorai.mobile.features.main.data.dto.userProfile.updateUser.UpdateUserProfileRequest
import dev.calorai.mobile.features.main.data.dto.userProfile.updateUser.UpdateUserProfileResponse
import dev.calorai.mobile.features.main.data.entity.UserEntity
import dev.calorai.mobile.features.main.data.mappers.toEntity
import dev.calorai.mobile.features.main.data.mappers.toGetUserProfileResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response

interface UserRepository {

    suspend fun getUserProfile(userId: Int): Response<GetUserProfileResponse>
    suspend fun updateUserProfile(request: UpdateUserProfileRequest): Response<UpdateUserProfileResponse>
    suspend fun createUserProfile(request: CreateUserProfileRequest): Response<Unit>

    fun observeUser(): Flow<UserEntity>
}

class UserRepositoryImpl(
    private val api: UserProfileApi,
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun getUserProfile(userId: Int): Response<GetUserProfileResponse> =
        withContext(dispatcher) {
            val local = userDao.getUser()
            if (local != null) {
                val localDto = local.toGetUserProfileResponse()
                return@withContext Response.success(localDto)
            }
            val response = api.getUserProfile(userId)
            if (response.isSuccessful) {
                val body = response.body()
                    ?: throw IllegalStateException("GetUserProfile: response successful but body is null")
                userDao.insert(body.toEntity())
            }
            response
        }

    override suspend fun updateUserProfile(request: UpdateUserProfileRequest): Response<UpdateUserProfileResponse> =
        withContext(dispatcher) {
            val userId = userDao.getUserId() ?: throw IllegalStateException("No userId found in DB")
            val response = api.updateUserProfile(userId, request)
            if (response.isSuccessful) {
                val body = response.body()
                    ?: throw IllegalStateException("UpdateUserProfile: response successful but body is null")
                userDao.update(body.toEntity())
            }
            response
        }

    override suspend fun createUserProfile(request: CreateUserProfileRequest): Response<Unit> =
        withContext(dispatcher) {
            val response = api.createUserProfile(request)
            if (response.isSuccessful) {
                userDao.insert(request.toEntity())
            }
            response
        }

    override fun observeUser(): Flow<UserEntity> {
        return userDao.observeUser()
    }
}
