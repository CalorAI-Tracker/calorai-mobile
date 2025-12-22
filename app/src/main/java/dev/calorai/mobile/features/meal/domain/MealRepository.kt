package dev.calorai.mobile.features.meal.domain

import dev.calorai.mobile.features.main.data.dao.UserDao
import dev.calorai.mobile.features.meal.data.api.MealApi
import dev.calorai.mobile.features.meal.data.dao.DailyMealsDao
import dev.calorai.mobile.features.meal.data.dto.createMealEntry.CreateMealEntryRequest
import dev.calorai.mobile.features.meal.data.dto.getDailyMeal.GetDailyMealResponse
import dev.calorai.mobile.features.meal.data.entity.DailyMealsEntity
import dev.calorai.mobile.features.meal.data.mappers.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response

interface MealRepository {

    suspend fun getDailyMealAndSync(date: String?): Response<GetDailyMealResponse>
    suspend fun createMealEntryAndSync(request: CreateMealEntryRequest): Response<Unit>

    fun observeMealsByDate(date: String): Flow<List<DailyMealsEntity>>
    fun observeMealByDateAndType(date: String, mealType: String): Flow<DailyMealsEntity?>
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

    override suspend fun getDailyMealAndSync(date: String?): Response<GetDailyMealResponse> =
        withContext(dispatcher) {
            val userId = userDao.getUserId() ?: throw IllegalStateException("No userId found in DB")
            val response = api.getDailyMeal(userId = userId, date = date)
            if (!response.isSuccessful) {
                return@withContext response
            }
            val body = response.body()
                ?: throw IllegalStateException("GetDailyMeal: response successful but body is null")
            val serverEntities = body.meals.map { it.toEntity(body.date) }
            val localEntities = dailyMealsDao.getMealsByDateOnce(body.date)
            if (!areMealListsEqual(localEntities, serverEntities)) {
                dailyMealsDao.deleteByDate(body.date)
                dailyMealsDao.insertAll(serverEntities)
            }
            response
        }

    override suspend fun createMealEntryAndSync(request: CreateMealEntryRequest): Response<Unit> =
        withContext(dispatcher) {
            val response = api.createMealEntry(request)
            if (!response.isSuccessful) return@withContext response
            getDailyMealAndSync(request.eatenAt)
            response
        }

    override fun observeMealsByDate(date: String): Flow<List<DailyMealsEntity>> =
        dailyMealsDao.getMealsByDate(date)

    override fun observeMealByDateAndType(date: String, mealType: String): Flow<DailyMealsEntity?> =
        dailyMealsDao.getMealByDateAndType(date, mealType)

    override suspend fun deleteMealById(id: Long) = withContext(dispatcher) {
        dailyMealsDao.deleteById(id)
    }

    override suspend fun deleteMealsByDate(date: String) = withContext(dispatcher) {
        dailyMealsDao.deleteByDate(date)
    }

    override suspend fun clearAllMeals() = withContext(dispatcher) {
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
