package dev.calorai.mobile

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.dto.login.LoginRequest
import dev.calorai.mobile.features.auth.data.dto.signup.SignupRequest
import dev.calorai.mobile.features.auth.data.token.InMemoryTokenProvider
import dev.calorai.mobile.features.auth.data.token.TokenStorage
import dev.calorai.mobile.features.main.data.api.DailyNutritionApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

class AuthAndTokenTests {

    private lateinit var authApi: AuthApi
    private lateinit var tokenStorage: TokenStorage
    private lateinit var tokenProvider: InMemoryTokenProvider
    private lateinit var dailyNutritionApi: DailyNutritionApi

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        tokenStorage = TokenStorage(context)

        val json = Json { ignoreUnknownKeys = true; isLenient = true }

        val okHttp = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                tokenProvider.getAccessToken()?.let { token ->
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/") // для эмулятора Android
            .client(okHttp)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        authApi = retrofit.create(AuthApi::class.java)
        dailyNutritionApi = retrofit.create(DailyNutritionApi::class.java)
        tokenProvider = InMemoryTokenProvider(authApi, tokenStorage)
    }

    @After
    fun teardown() {
        tokenStorage.clearTokens()
    }

    @Test
    fun signupTest() = runBlocking {
        val request = SignupRequest(
            email = "testttuser@example.com",
            password = "password123"
        )
        val response = authApi.signUp(request)
        assertEquals(request.email, response.email)
    }

    @Test
    fun loginAndTokenTest() = runBlocking {
        val request = LoginRequest(
            email = "testuser@example.com",
            password = "password123",
            deviceId = "test-device-1"
        )
        val response = authApi.login(request)

        tokenProvider.setTokens(response.accessToken, response.refreshToken)

        val storedAccess = tokenProvider.getAccessToken()
        val refreshSuccess = tokenProvider.refreshTokenBlocking()

        assertEquals(response.accessToken, storedAccess)
        assert(refreshSuccess)
    }

    @Test
    fun dailyNutritionApiTest() = runBlocking {
        // Логинимся и устанавливаем токены
        val loginRequest = LoginRequest(
            email = "testuser@example.com",
            password = "password123",
            deviceId = "test-device-1"
        )
        val loginResponse = authApi.login(loginRequest)
        tokenProvider.setTokens(loginResponse.accessToken, loginResponse.refreshToken)

        // Делаем запрос к DailyNutritionApi с прокидыванием токена
        val response = dailyNutritionApi.getDailyStats(
            userId = 1,
            date = "2025-12-13"
        )

        assertEquals("2025-12-13", response.date)
    }
}