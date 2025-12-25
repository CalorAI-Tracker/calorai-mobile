package dev.calorai.mobile.features.auth.data.repository

import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.dto.logout.LogoutRequest
import dev.calorai.mobile.features.auth.data.mapper.AuthMapper
import dev.calorai.mobile.features.auth.data.token.CredentialsStore
import dev.calorai.mobile.features.auth.domain.AuthRepository
import dev.calorai.mobile.features.auth.domain.model.LoginPayload
import dev.calorai.mobile.features.auth.domain.model.SignupPayload
import dev.calorai.mobile.features.profile.data.dao.UserDao
import dev.calorai.mobile.features.profile.domain.UserIdStore
import dev.calorai.mobile.features.profile.domain.model.UserId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AuthRepositoryImpl constructor(
    private val api: AuthApi,
    private val credentialsStore: CredentialsStore,
    private val userIdStore: UserIdStore,
    private val userDao: UserDao,
    private val mapper: AuthMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AuthRepository {

    override suspend fun signUp(payload: SignupPayload) = withContext(dispatcher) {
        val response = api.signUp(mapper.mapToRequest(payload))
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        val id = response.body()?.id
            ?: throw IllegalStateException("SignUp response body is null")
        userIdStore.setUserId(UserId(id))
    }

    override suspend fun login(payload: LoginPayload) = withContext(dispatcher) {
        val response = api.login(mapper.mapToRequest(payload))
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        val body = response.body()
            ?: throw IllegalStateException("Login response is successful but body is null")
        credentialsStore.setCredentials(body.accessToken, body.refreshToken)
        userIdStore.setUserId(UserId(body.id))
    }

    override suspend fun refreshToken(refreshToken: String) = withContext(dispatcher) {
        val response = api.refresh(mapper.mapToRequest(refreshToken))
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        val body = response.body()
            ?: throw IllegalStateException("Refresh response is successful but body is null")
        credentialsStore.setCredentials(body.accessToken, body.refreshToken)
    }

    override suspend fun logout() = withContext(dispatcher) {
        val refresh = credentialsStore.getRefreshToken()
            ?: throw IllegalStateException("No refresh token available for logout")
        val response = api.logout(LogoutRequest(refresh))
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        credentialsStore.clearCredentials()
        userIdStore.clearUserId()
        userDao.clear()
    }
}
