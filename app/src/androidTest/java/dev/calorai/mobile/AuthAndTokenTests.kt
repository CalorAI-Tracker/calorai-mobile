package dev.calorai.mobile

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.calorai.mobile.core.network.ErrorResponseInterceptor
import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.dto.login.LoginRequest
import dev.calorai.mobile.features.auth.data.dto.login.LoginResponse
import dev.calorai.mobile.features.auth.data.dto.signup.SignupRequest
import dev.calorai.mobile.features.auth.data.dto.signup.SignupResponse
import dev.calorai.mobile.features.auth.data.token.AuthInterceptor
import dev.calorai.mobile.features.auth.data.token.tokenProvider.InMemoryTokenProvider
import dev.calorai.mobile.features.auth.data.token.TokenAuthenticator
import dev.calorai.mobile.features.auth.data.token.TokenRefresher
import dev.calorai.mobile.features.auth.data.token.TokenStorage
import dev.calorai.mobile.features.meal.data.api.MealApi
import dev.calorai.mobile.features.meal.data.dto.getDailyMeal.GetDailyMealResponse
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Assert.assertTrue
import retrofit2.Response

class AuthAndTokenTests {

    private lateinit var authApi: AuthApi
    private lateinit var tokenStorage: TokenStorage
    private lateinit var tokenRefresher: TokenRefresher
    private lateinit var tokenProvider: InMemoryTokenProvider
    private lateinit var mealApi: MealApi

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        tokenStorage = TokenStorage(context)
        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        val okHttpAuth = OkHttpClient.Builder()
            .addNetworkInterceptor(ErrorResponseInterceptor(json))
            .build()
        val retrofitAuth = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/") // для эмулятора Android
            .client(okHttpAuth)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json; charset=utf-8".toMediaType()
                )
            )
            .build()
        authApi = retrofitAuth.create(AuthApi::class.java)
        tokenProvider = InMemoryTokenProvider(
            tokenStorage
        )
        tokenRefresher = TokenRefresher(
            authApi,
            tokenStorage,
        )
        val okHttpAuthorized = OkHttpClient.Builder()
            .addNetworkInterceptor(ErrorResponseInterceptor(json))
            .addInterceptor(AuthInterceptor(tokenProvider))
            .authenticator(
                TokenAuthenticator(
                    tokenProvider,
                    tokenRefresher,
                )
            )
            .build()
        val retrofitAuthorized = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/api/") // для эмулятора Android
            .client(okHttpAuthorized)
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=utf-8".toMediaType()
                )
            )
            .build()
        mealApi = retrofitAuthorized.create(MealApi::class.java)
    }

    @After
    fun teardown() {
        tokenStorage.clearTokens()
    }

    @Test
    fun signupTest() = runBlocking {
        val request = SignupRequest(
            email = "test_user_1@example.com",
            password = "password123"
        )
        val response: Response<SignupResponse> = authApi.signUp(request)
        assertTrue(response.isSuccessful)
        val body = response.body()
        requireNotNull(body)
        assertEquals(request.email, body.email)
    }

    @Test
    fun loginAndTokenTest() = runBlocking {
        val request = LoginRequest(
            email = "testuser@example.com",
            password = "password123",
            deviceId = "test-device-1"
        )
        val response: Response<LoginResponse> = authApi.login(request)
        assertTrue(response.isSuccessful)
        val body = response.body()
        requireNotNull(body)
        tokenStorage.setTokens(
            accessToken = body.accessToken,
            refreshToken = body.refreshToken
        )
        val storedAccessToken = tokenProvider.getAccessToken()
        assertEquals(body.accessToken, storedAccessToken)
        val refreshSuccess = tokenRefresher.refreshTokenBlocking()
        assertTrue(refreshSuccess)
    }

    @Test
    fun dailyNutritionApiTest() = runBlocking {
        val loginRequest = LoginRequest(
            email = "string",
            password = "string",
            deviceId = "test-device-1"
        )
        val loginResponse: Response<LoginResponse> = authApi.login(loginRequest)
        assertTrue(loginResponse.isSuccessful)
        val loginBody = loginResponse.body()
        requireNotNull(loginBody)
        tokenStorage.setTokens(
            loginBody.accessToken,
            loginBody.refreshToken)

        // Делаем запрос к MealApi с прокидыванием токена
        val getDailyMealResponse: Response<GetDailyMealResponse> = mealApi.getDailyMeal(
            userId = 2,
            date = "2025-12-09"
        )
        assertTrue(getDailyMealResponse.isSuccessful)
        val getDailyMealBody = getDailyMealResponse.body()!!
        requireNotNull(loginBody)
        assertEquals("2025-12-09", getDailyMealBody.date)
    }
}
