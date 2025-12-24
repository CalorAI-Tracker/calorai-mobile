package dev.calorai.mobile.features.meal.data.repository

import dev.calorai.mobile.features.meal.data.api.MealApi
import dev.calorai.mobile.features.meal.data.dao.DailyMealsDao
import dev.calorai.mobile.features.meal.data.entity.DailyMealsEntity
import dev.calorai.mobile.features.meal.data.mappers.MealMapper
import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.CreateMealEntryPayload
import dev.calorai.mobile.features.meal.domain.model.DailyMeal
import dev.calorai.mobile.features.profile.data.dao.UserDao
import dev.calorai.mobile.features.profile.domain.model.UserId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MealRepositoryImpl(
    private val api: MealApi,
    private val dailyMealsDao: DailyMealsDao,
    private val userDao: UserDao,
    private val mapper: MealMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MealRepository {

    private suspend fun syncDailyMeals(date: String) {
        val userId = userDao.getUserId()
            ?: throw IllegalStateException("No userId found in DB")
        val response = api.getDailyMeal(userId = userId, date = date)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        val body = response.body()
            ?: throw IllegalStateException("SyncDailyMeals: body is null")
        val serverEntities = body.meals.map { mapper.mapToEntity(it,body.date) }
        val localEntities = dailyMealsDao.getMealsByDateOnce(body.date)
        if (!areMealListsEqual(localEntities, serverEntities)) {
            dailyMealsDao.deleteByDate(body.date)
            dailyMealsDao.insertAll(serverEntities)
        }
    }

    override suspend fun getDailyMeals(date: String): List<DailyMeal> = withContext(dispatcher) {
        try {
            val userId = UserId(userDao.getUserId() ?: throw IllegalStateException("User not found"))
            val response = api.getDailyMeal(userId = userId.value, date = date)
            if(!response.isSuccessful) {
                return@withContext getDailyMealsLocal(date)
            }
            val dailyMealsResponse = response.body() ?: throw IllegalStateException()
            val dailyMeals = mapper.mapToDomain(dailyMealsResponse)
            dailyMealsDao.insertAll(mapper.mapToEntity(dailyMeals))
            return@withContext dailyMeals
        } catch (_: Exception) {
            return@withContext getDailyMealsLocal(date)
        }
    }

    private suspend fun getDailyMealsLocal(date: String): List<DailyMeal> =
        withContext(dispatcher) {
            dailyMealsDao
                .getMealsByDateOnce(date)
                .map { mapper.mapToDomain(it) }
        }

    override suspend fun createMealEntryAndSync(payload: CreateMealEntryPayload) =
        withContext(dispatcher) {
            val response = api.createMealEntry(mapper.mapToRequest(payload))
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            syncDailyMeals(payload.eatenAt)
        }

    override fun observeMealsByDate(date: String): Flow<List<DailyMeal>> =
        dailyMealsDao
            .getMealsByDate(date)
            .map { entities ->
                entities.map { mapper.mapToDomain(it) }
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
