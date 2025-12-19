package dev.calorai.mobile.features.main.features.plan.data.api

import dev.calorai.mobile.features.main.features.plan.data.dto.getActivityLevels.GetActivityLevelsResponse
import dev.calorai.mobile.features.main.features.plan.data.dto.getHealthGoals.GetHealthGoalsResponse
import retrofit2.Response
import retrofit2.http.GET

interface PlanApi {

    @GET("/meta/health-goals")
    suspend fun getHealthGoals(): Response<GetHealthGoalsResponse>

    @GET("/meta/activity-levels")
    suspend fun getActivityLevels(): Response<GetActivityLevelsResponse>
}
