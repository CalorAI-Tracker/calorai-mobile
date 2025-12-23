package dev.calorai.mobile.features.auth.domain

import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.dto.login.LoginRequest
import dev.calorai.mobile.features.auth.data.dto.logout.LogoutRequest
import dev.calorai.mobile.features.auth.data.dto.refresh.RefreshRequest
import dev.calorai.mobile.features.auth.data.dto.signup.SignupRequest
import dev.calorai.mobile.features.auth.data.token.TokenStorage
import dev.calorai.mobile.features.main.data.dao.UserDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface AuthRepository {

    suspend fun signUp(request: SignupRequest): Int
    suspend fun login(request: LoginRequest)
    suspend fun refreshToken(request: RefreshRequest)
    suspend fun logout()
}

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage,
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : AuthRepository {

    override suspend fun signUp(request: SignupRequest): Int =
        withContext(dispatcher) {
            val response = api.signUp(request)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            response.body()?.id
                ?: throw IllegalStateException("SignUp response body is null")
        }

    override suspend fun login(request: LoginRequest) =
        withContext(dispatcher) {
            val response = api.login(request)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            val body = response.body()
                ?: throw IllegalStateException("Login response is successful but body is null")
            tokenStorage.setTokens(body.accessToken, body.refreshToken)
        }

    override suspend fun refreshToken(request: RefreshRequest) =
        withContext(dispatcher) {
            val response = api.refresh(request)
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
