package dev.calorai.mobile.features.main.data.api

import dev.calorai.mobile.features.main.data.dto.dailyNutrition.getDailyStats.GetDailyStatsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DailyNutritionApi {
    @GET("daily-nutrition/{userId}")
    suspend fun getDailyStats(
        @Path("userId") userId: Int,
        @Query("date") date: String? = null
    ): GetDailyStatsResponse

}