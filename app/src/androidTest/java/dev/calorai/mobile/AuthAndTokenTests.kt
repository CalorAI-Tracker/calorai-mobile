package dev.calorai.mobile

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.dto.login.LoginRequest
import dev.calorai.mobile.features.auth.data.dto.signup.SignupRequest
import dev.calorai.mobile.features.auth.data.token.AuthInterceptor
import dev.calorai.mobile.features.auth.data.token.InMemoryTokenProvider
import dev.calorai.mobile.features.auth.data.token.TokenAuthenticator
import dev.calorai.mobile.features.auth.data.token.TokenStorage
import dev.calorai.mobile.features.main.data.api.DailyNutritionApi
import dev.calorai.mobile.features.meal.data.api.MealApi
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
    private lateinit var mealApi: MealApi

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        tokenStorage = TokenStorage(context)

        val json = Json { ignoreUnknownKeys = true; isLenient = true }

        val retrofitAuth = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/") // для эмулятора Android
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        authApi = retrofitAuth.create(AuthApi::class.java)
        tokenProvider = InMemoryTokenProvider(authApi, tokenStorage)

        val okHttp = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider))
            .authenticator(TokenAuthenticator(tokenProvider))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/") // для эмулятора Android
            .client(okHttp)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        mealApi = retrofit.create(MealApi::class.java)
    }

    @After
    fun teardown() {
        tokenStorage.clearTokens()
    }

    @Test
    fun signupTest() = runBlocking {
        val request = SignupRequest(
            email = "tttttееestttuser@example.com",
            password = "password123"
        )
        val response = authApi.signUp(request)
        assertEquals(request.email, response.email)
    }

    @Test
    fun loginAndTokenTest() = runBlocking {
        val request = LoginRequest(
            email = "ttеestttuser@example.com",
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
            email = "string",
            password = "string",
            deviceId = "test-device-1"
        )
        val loginResponse = authApi.login(loginRequest)
        tokenProvider.setTokens(loginResponse.accessToken, loginResponse.refreshToken)

        // Делаем запрос к MealApi с прокидыванием токена
        val response = mealApi.getDailyMeal(
            userId = 2,
            date = "2025-12-11"
        )

        assertEquals("2025-12-11", response.date)
    }
}