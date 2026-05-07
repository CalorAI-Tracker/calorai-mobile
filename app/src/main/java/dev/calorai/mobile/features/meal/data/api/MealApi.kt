package dev.calorai.mobile.features.meal.data.api

import dev.calorai.mobile.features.meal.data.dto.getDailyMeal.GetDailyMealResponse
import dev.calorai.mobile.features.meal.data.dto.getDailyMealsComposition.GetDailyMealsCompositionResponse
import dev.calorai.mobile.features.meal.data.dto.searchFoodCatalog.SearchFoodCatalogRequest
import dev.calorai.mobile.features.meal.data.dto.searchFoodCatalog.SearchFoodCatalogResponse
import dev.calorai.mobile.features.meal.data.dto.mealEntry.MealEntryRecognizeResponse
import dev.calorai.mobile.features.meal.data.dto.mealEntry.MealEntryRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface MealApi {

    @GET("$DAILY_MEAL")
    suspend fun getDailyMeal(
        @Query("date") date: String,
    ): Response<GetDailyMealResponse>

    @POST("$FOOD_DIARY/entries")
    suspend fun createMealEntry(
        @Body body: MealEntryRequest,
    ): Response<Unit>

    @POST("food-catalog/search")
    suspend fun searchFoodCatalog(
        @Body body: SearchFoodCatalogRequest,
    ): Response<SearchFoodCatalogResponse>

    @PUT("$FOOD_DIARY/entries/{entryId}")
    suspend fun updateMealEntry(
        @Path("entryId") entryId: Long,
        @Body body: MealEntryRequest,
    ): Response<Unit>

    @DELETE("$FOOD_DIARY/entries/{entryId}")
    suspend fun deleteMealEntry(
        @Path("entryId") entryId: Long,
    ): Response<Unit>

    @GET("$FOOD_DIARY/composition")
    suspend fun getDailyMealsComposition(
        @Query("date") date: String,
    ): Response<GetDailyMealsCompositionResponse>

    @Multipart
    @POST("food/recognize")
    suspend fun mealRecognize(
        @Part image: MultipartBody.Part,
    ): Response<MealEntryRecognizeResponse>

    private companion object {
        private const val FOOD_DIARY = "food-diary"
        private const val DAILY_MEAL = "daily-meal"
    }
}
