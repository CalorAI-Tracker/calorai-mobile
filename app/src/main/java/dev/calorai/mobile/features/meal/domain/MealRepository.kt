package dev.calorai.mobile.features.meal.domain

import dev.calorai.mobile.features.main.data.dao.UserDao
import dev.calorai.mobile.features.meal.data.api.MealApi
import dev.calorai.mobile.features.meal.data.dao.DailyMealsDao
import dev.calorai.mobile.features.meal.data.dto.createMealEntry.CreateMealEntryRequest
import dev.calorai.mobile.features.meal.data.entity.DailyMealsEntity
import dev.calorai.mobile.features.meal.data.mappers.toDomain
import dev.calorai.mobile.features.meal.data.mappers.toEntity
import dev.calorai.mobile.features.meal.domain.model.DailyMeal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface MealRepository {

    suspend fun syncDailyMeals(date: String)
    suspend fun getDailyMealsRemote(date: String): List<DailyMeal>
    suspend fun getDailyMealsLocal(date: String): List<DailyMeal>
    suspend fun createMealEntryAndSync(request: CreateMealEntryRequest)
    fun observeMealsByDate(date: String): Flow<List<DailyMeal>>
    fun observeMealByDateAndType(date: String, mealType: String): Flow<DailyMeal?>
    suspend fun deleteMealById(id: Long)
    suspend fun deleteMealsByDate(date: String)
    suspend fun clearAllMeals()
}

class MealRepositoryImpl(
    private val api: MealApi,
    private val dailyMealsDao: DailyMealsDao,
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MealRepository {

    override suspend fun syncDailyMeals(date: String) {
        val userId = userDao.getUserId()
            ?: throw IllegalStateException("No userId found in DB")
        val response = api.getDailyMeal(userId = userId, date = date)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        val body = response.body()
            ?: throw IllegalStateException("SyncDailyMeals: body is null")
        val serverEntities = body.meals.map { it.toEntity(body.date) }
        val localEntities = dailyMealsDao.getMealsByDateOnce(body.date)
        if (!areMealListsEqual(localEntities, serverEntities)) {
            dailyMealsDao.deleteByDate(body.date)
            dailyMealsDao.insertAll(serverEntities)
        }
    }

    override suspend fun getDailyMealsRemote(date: String): List<DailyMeal> =
        withContext(dispatcher) {
            syncDailyMeals(date)
            dailyMealsDao
                .getMealsByDateOnce(date)
                .map { it.toDomain() }
        }

    override suspend fun getDailyMealsLocal(date: String): List<DailyMeal> =
        withContext(dispatcher) {
            dailyMealsDao
                .getMealsByDateOnce(date)
                .map { it.toDomain() }
        }

    override suspend fun createMealEntryAndSync(request: CreateMealEntryRequest) =
        withContext(dispatcher) {
            val response = api.createMealEntry(request)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            syncDailyMeals(request.eatenAt)
        }

    override fun observeMealsByDate(date: String): Flow<List<DailyMeal>> =
        dailyMealsDao
            .getMealsByDate(date)
            .map { entities ->
                entities.map { it.toDomain() }
            }

    override fun observeMealByDateAndType(
        date: String,
        mealType: String
    ): Flow<DailyMeal?> =
        dailyMealsDao
            .getMealByDateAndType(date, mealType)
            .map { entity ->
                entity?.toDomain()
            }

    override suspend fun deleteMealById(id: Long) =
        withContext(dispatcher) {
            dailyMealsDao.deleteById(id)
        }

    override suspend fun deleteMealsByDate(date: String) =
        withContext(dispatcher) {
            dailyMealsDao.deleteByDate(date)
        }

    override suspend fun clearAllMeals() =
        withContext(dispatcher) {
            dailyMealsDao.clearAllMeals()
        }
}

private fun areMealListsEqual(
    local: List<DailyMealsEntity>,
    server: List<DailyMealsEntity>
): Boolean {
    if (local.size != server.size) return false
    val localSorted = local.sortedBy { it.meal }
    val serverSorted = server.sortedBy { it.meal }
    return localSorted == serverSorted
}
