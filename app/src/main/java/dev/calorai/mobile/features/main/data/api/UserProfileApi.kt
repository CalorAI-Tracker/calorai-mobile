package dev.calorai.mobile.features.main.data.api

import dev.calorai.mobile.features.main.data.dto.userProfile.createUser.CreateUserProfileRequest
import dev.calorai.mobile.features.main.data.dto.userProfile.getUser.GetUserProfileResponse
import dev.calorai.mobile.features.main.data.dto.userProfile.updateUser.UpdateUserProfileRequest
import dev.calorai.mobile.features.main.data.dto.userProfile.updateUser.UpdateUserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserProfileApi {
    @GET("user-profile/user/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: Int,
    ): Response<GetUserProfileResponse>

    @PUT("user-profile/user/{userId}")
    suspend fun updateUserProfile(
        @Path("userId") userId: Int,
        @Body body: UpdateUserProfileRequest,
    ): Response<UpdateUserProfileResponse>

    @POST("user-profile")
    suspend fun createUserProfile(
        @Body body: CreateUserProfileRequest,
    ): Response<Unit>
}