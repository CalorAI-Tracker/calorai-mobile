package dev.calorai.mobile.features.meal.data.repository

import dev.calorai.mobile.features.meal.data.api.MealApi
import dev.calorai.mobile.features.meal.data.dao.DailyMealsDao
import dev.calorai.mobile.features.meal.data.dao.IngredientsDao
import dev.calorai.mobile.features.meal.data.entity.IngredientsEntity
import dev.calorai.mobile.features.meal.data.mappers.MealMapper
import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.DailyMeal
import dev.calorai.mobile.features.meal.domain.model.MealEntry
import dev.calorai.mobile.features.meal.domain.model.MealEntryId
import dev.calorai.mobile.features.meal.domain.model.MealEntryPayload
import dev.calorai.mobile.features.meal.domain.model.MealId
import dev.calorai.mobile.features.meal.domain.model.MealRecognizeEntry
import dev.calorai.mobile.features.meal.domain.model.MealType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Response

class MealRepositoryImpl constructor(
    private val api: MealApi,
    private val dailyMealsDao: DailyMealsDao,
    private val ingredientsDao: IngredientsDao,
    private val mapper: MealMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MealRepository {

    override suspend fun getDailyMeals(date: String): List<DailyMeal> = withContext(dispatcher) {
        try {
            syncDailyMealWithApi(date = date)
            return@withContext getDailyMealsLocal(date)
        } catch (_: Exception) {
            return@withContext getDailyMealsLocal(date)
        }
    }

    override suspend fun createMealEntryAndSync(payload: MealEntryPayload) =
        withContext(dispatcher) {
            try {
                api.createMealEntry(body = mapper.mapToRequest(payload)).throwOnError()
                syncDailyMealWithApi(date = payload.eatenAt)
                syncMealIngredientsWithApi(date = payload.eatenAt)
            } catch (_: Exception) {
            }
        }

    override suspend fun getMealEntry(mealEntryId: MealEntryId): MealEntry =
        withContext(dispatcher) {
            requireNotNull(ingredientsDao.getById(id = mealEntryId.value))
                .let(mapper::mapToDomain)
        }

    override suspend fun updateMealEntryAndSync(
        mealEntryId: MealEntryId,
        payload: MealEntryPayload,
    ) = withContext(dispatcher) {
        try {
            api.updateMealEntry(
                entryId = mealEntryId.value,
                body = mapper.mapToRequest(payload),
            ).throwOnError()
            syncDailyMealWithApi(date = payload.eatenAt)
            syncMealIngredientsWithApi(date = payload.eatenAt)
        } catch (_: Exception) {
        }
    }

    override suspend fun deleteMealEntryAndSync(
        mealEntryId: MealEntryId,
        date: String,
    ) = withContext(dispatcher) {
        try {
            api.deleteMealEntry(entryId = mealEntryId.value).throwOnError()
            syncDailyMealWithApi(date = date)
            syncMealIngredientsWithApi(date = date)
        } catch (_: Exception) {
        }
    }

    override suspend fun getMealIngredients(
        date: String,
        mealType: MealType,
    ): List<MealEntry> = withContext(dispatcher) {
        try {
            syncMealIngredientsWithApi(date = date)
            return@withContext getMealIngredientsLocal(date, mealType)
        } catch (_: Exception) {
            return@withContext getMealIngredientsLocal(date, mealType)
        }
    }

    override suspend fun deleteMealById(id: MealId) = withContext(dispatcher) {
        dailyMealsDao.deleteById(id.value)
    }

    override suspend fun deleteMealsByDate(date: String) = withContext(dispatcher) {
        dailyMealsDao.deleteByDate(date)
        ingredientsDao.deleteByDate(date)
    }

    override suspend fun deleteMeal(date: String, mealType: MealType) = withContext(dispatcher) {
        try {
            getMealIngredients(date, mealType).forEach { mealEntry ->
                api.deleteMealEntry(entryId = mealEntry.id.value).throwOnError()
            }
            syncDailyMealWithApi(date = date)
            syncMealIngredientsWithApi(date = date)
        } catch (_: Exception) {
        }
    }

    override suspend fun clearAllMeals() = withContext(dispatcher) {
        dailyMealsDao.clearAllMeals()
        ingredientsDao.clearAll()
    }

    override suspend fun mealRecognize(image: ByteArray): MealRecognizeEntry =
        withContext(dispatcher) {
            val contentType = "image/jpeg".toMediaTypeOrNull()
            val requestFile = image.toRequestBody(contentType)
            val body = MultipartBody.Part.createFormData(
                name = "image",
                filename = "meal.jpg",
                body = requestFile,
            )
            return@withContext api.mealRecognize(image = body).getOrThrow().let(mapper::mapToDomain)
        }

    private suspend fun syncDailyMealWithApi(date: String) = withContext(dispatcher) {
        val apiEntities = api.getDailyMeal(date).getOrThrow().meals.map { mealDto ->
            mapper.mapToEntity(
                dto = mealDto,
                date = date,
            )
        }
        val localEntities = dailyMealsDao.getMealsByDateOnce(date = date)
        if (!localEntities.areListsEqual(apiEntities)) {
            dailyMealsDao.deleteByDate(date)
            if (apiEntities.isNotEmpty()) {
                dailyMealsDao.insertAll(apiEntities)
            }
        }
    }

    private suspend fun syncMealIngredientsWithApi(date: String) = withContext(dispatcher) {
        val apiComposition = api.getDailyMealsComposition(date).getOrThrow()
        val localComposition = ingredientsDao.getByDateOnce(date).groupBy { it.mealType }
        if (localComposition.size != apiComposition.meals.size) {
            deleteMealsByDate(date)
        }
        apiComposition.meals.forEach { (mealType, entries) ->
            val apiEntities: List<IngredientsEntity> = entries.map { mealEntryDto ->
                mapper.mapToEntity(
                    dto = mealEntryDto,
                    date = date,
                    mealType = mealType,
                )
            }
            val localEntities = ingredientsDao.getByDateAndMealTypeOnce(
                date = date,
                mealType = mealType,
            )
            if (!localEntities.areListsEqual(apiEntities)) {
                ingredientsDao.deleteByDateAndMealType(
                    date = date,
                    mealType = mealType,
                )
                if (apiEntities.isNotEmpty()) {
                    ingredientsDao.insertAll(apiEntities)
                }
            }
        }
    }

    private suspend fun getDailyMealsLocal(date: String): List<DailyMeal> =
        withContext(dispatcher) {
            dailyMealsDao
                .getMealsByDateOnce(date)
                .map(mapper::mapToDomain)
        }

    private suspend fun getMealIngredientsLocal(
        date: String,
        mealType: MealType,
    ): List<MealEntry> = withContext(dispatcher) {
        ingredientsDao
            .getByDateAndMealTypeOnce(date = date, mealType = mealType.name)
            .map(mapper::mapToDomain)
    }

    private fun <T> Response<T>.getOrThrow(): T {
        throwOnError()
        return requireNotNull(this.body())
    }

    private fun Response<*>.throwOnError() {
        if (!this.isSuccessful) throw HttpException(this)
    }

    private fun <T> List<T>.areListsEqual(other: List<T>): Boolean {
        if (size != other.size) return false
        return toSet() == other.toSet()
    }
}
