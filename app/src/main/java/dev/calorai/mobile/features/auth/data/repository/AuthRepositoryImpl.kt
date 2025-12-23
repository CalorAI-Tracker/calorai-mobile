package dev.calorai.mobile.features.auth.data.repository

import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.dto.logout.LogoutRequest
import dev.calorai.mobile.features.auth.data.mapper.AuthMapper
import dev.calorai.mobile.features.auth.data.token.TokenStorage
import dev.calorai.mobile.features.auth.domain.AuthRepository
import dev.calorai.mobile.features.auth.domain.model.LoginPayload
import dev.calorai.mobile.features.auth.domain.model.SignupPayload
import dev.calorai.mobile.features.profile.data.dao.UserDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage,
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AuthRepository {

    private val mapper = AuthMapper()

    override suspend fun signUp(payload: SignupPayload): Int =
        withContext(dispatcher) {
            val response = api.signUp(mapper.mapToRequest(payload))
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            response.body()?.id
                ?: throw IllegalStateException("SignUp response body is null")
        }

    override suspend fun login(payload: LoginPayload) =
        withContext(dispatcher) {
            val response = api.login(mapper.mapToRequest(payload))
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            val body = response.body()
                ?: throw IllegalStateException("Login response is successful but body is null")
            tokenStorage.setTokens(body.accessToken, body.refreshToken)
        }

    override suspend fun refreshToken(refreshToken: String) =
        withContext(dispatcher) {
            val response = api.refresh(mapper.mapToRequest(refreshToken))
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            val body = response.body()
                ?: throw IllegalStateException("Refresh response is successful but body is null")
            tokenStorage.setTokens(body.accessToken, body.refreshToken)
        }

    override suspend fun logout() =
        withContext(dispatcher) {
            val refresh = tokenStorage.getRefreshToken()
                ?: throw IllegalStateException("No refresh token available for logout")
            val response = api.logout(LogoutRequest(refresh))
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            tokenStorage.clearTokens()
            userDao.clear()
        }
}
