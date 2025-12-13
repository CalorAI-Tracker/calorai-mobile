package dev.calorai.mobile.features.meal.data.api

import dev.calorai.mobile.features.meal.data.dto.createMealEntry.CreateMealEntryRequest
import dev.calorai.mobile.features.meal.data.dto.getDailyMeal.GetDailyMealResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface MealApi {
    @GET("daily-meal/{userId}")
    suspend fun getDailyMeal(
        @Path("userId") userId: Int,
        @Query("date") date: String? = null
    ): GetDailyMealResponse

    @POST("food-diary/{userId}/entries")
    suspend fun createMealEntry(
        @Body body: CreateMealEntryRequest
    ): Unit
}
