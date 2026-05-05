package dev.calorai.mobile.features.meal.domain.usecases

import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.model.FoodCatalogSearchPage

interface SearchFoodCatalogUseCase {
    suspend operator fun invoke(
        search: String,
        page: Int,
        size: Int,
    ): FoodCatalogSearchPage
}

class SearchFoodCatalogUseCaseImpl(
    private val repository: MealRepository,
) : SearchFoodCatalogUseCase {

    override suspend fun invoke(
        search: String,
        page: Int,
        size: Int,
    ): FoodCatalogSearchPage = repository.searchFoodCatalog(
        search = search,
        page = page,
        size = size,
    )
}
