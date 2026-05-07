package dev.calorai.mobile.features.meal.ready.ui

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.toRoute
import dev.calorai.mobile.R
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.meal.details.navigateToMealDetailsScreen
import dev.calorai.mobile.features.meal.domain.model.FoodCatalogItem
import dev.calorai.mobile.features.meal.domain.model.FoodCatalogSearchPage
import dev.calorai.mobile.features.meal.domain.model.MealEntryPayload
import dev.calorai.mobile.features.meal.domain.usecases.CreateMealEntryUseCase
import dev.calorai.mobile.features.meal.domain.usecases.SearchFoodCatalogUseCase
import dev.calorai.mobile.features.meal.ready.MealReadyListRoute
import dev.calorai.mobile.features.meal.ready.MealReadyListSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@OptIn(FlowPreview::class)
class MealReadyListViewModel(
    savedStateHandle: SavedStateHandle,
    private val context: Context,
    private val searchFoodCatalogUseCase: SearchFoodCatalogUseCase,
    private val createMealEntryUseCase: CreateMealEntryUseCase,
    private val uiMapper: MealReadyUiMapper,
    private val globalRouter: Router,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<MealReadyListRoute>()

    private val _uiState = MutableStateFlow<MealReadyListUiState>(MealReadyListUiState.Loading)
    val uiState: StateFlow<MealReadyListUiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("").also { query ->
        query
            .drop(1)
            .debounce(300)
            .distinctUntilChanged()
            .onEach(::loadFirstPage)
            .launchIn(viewModelScope)
    }
    private var currentItemsById: Map<String, FoodCatalogItem> = emptyMap()

    init {
        loadFirstPage(search = "")
    }

    fun onEvent(event: MealReadyListUiEvent) {
        when (event) {
            MealReadyListUiEvent.BackClick -> onBackClick()
            MealReadyListUiEvent.AddClick -> onAddClick()
            MealReadyListUiEvent.LoadNextPage -> onLoadNextPage()
            is MealReadyListUiEvent.MealClick -> onMealClick(event.id)
            is MealReadyListUiEvent.QueryChange -> onQueryChange(event.value)
        }
    }

    private fun onBackClick() {
        viewModelScope.launch {
            globalRouter.emit {
                if (route.source == MealReadyListSource.DETAILS) {
                    navigateToMealDetailsScreen(
                        mealType = route.mealType,
                        date = route.date,
                        navOptions = NavOptions.Builder()
                            .setPopUpTo<MealReadyListRoute>(inclusive = true)
                            .build(),
                    )
                } else {
                    popBackStack()
                }
            }
        }
    }

    private fun onQueryChange(value: String) {
        _uiState.update { current ->
            when (current) {
                MealReadyListUiState.Loading -> MealReadyListUiState.Ready(
                    meals = emptyList(),
                    query = value,
                )

                is MealReadyListUiState.Ready -> current.copy(
                    query = value,
                    selectedMealId = null,
                    isAppending = false,
                    canLoadMore = false,
                )
            }
        }
        searchQuery.value = value
    }

    private fun onMealClick(id: String) {
        _uiState.update { current ->
            when (current) {
                MealReadyListUiState.Loading -> current
                is MealReadyListUiState.Ready -> current.copy(selectedMealId = id)
            }
        }
    }

    private fun onLoadNextPage() {
        val currentState = _uiState.value as? MealReadyListUiState.Ready ?: return
        if (currentState.isAppending || !currentState.canLoadMore) return

        loadPage(
            search = currentState.query,
            page = currentState.currentPage + 1,
            append = true,
        )
    }

    private fun onAddClick() {
        val currentState = _uiState.value as? MealReadyListUiState.Ready ?: return
        val selectedId = currentState.selectedMealId ?: return
        val selectedMeal = currentItemsById[selectedId] ?: return

        viewModelScope.launch {
            runCatching {
                createMealEntryUseCase(
                    MealEntryPayload(
                        entryName = selectedMeal.name,
                        meal = route.mealType,
                        eatenAt = route.date,
                        proteinPerBaseG = selectedMeal.proteinPer100g,
                        fatPerBaseG = selectedMeal.fatPer100g,
                        carbsPerBaseG = selectedMeal.carbsPer100g,
                        baseQuantityGrams = 100.0,
                        portionQuantityGrams = 100.0,
                        brand = selectedMeal.brand,
                        barcode = selectedMeal.barcode,
                    )
                )
            }.onSuccess {
                globalRouter.emit {
                    navigateToMealDetailsScreen(
                        mealType = route.mealType,
                        date = route.date,
                        navOptions = NavOptions.Builder()
                            .setPopUpTo<MealReadyListRoute>(inclusive = true)
                            .build(),
                    )
                }
            }
        }
    }

    private fun loadFirstPage(search: String) {
        loadPage(search = search, page = FIRST_PAGE, append = false)
    }

    private fun loadPage(
        search: String,
        page: Int,
        append: Boolean,
    ) {
        viewModelScope.launch {
            preparePageLoad(append = append)

            runCatching {
                searchFoodCatalogUseCase(
                    search = search,
                    page = page,
                    size = PAGE_SIZE,
                )
            }
                .onSuccess { result ->
                    handleLoadPageSuccess(
                        search = search,
                        page = page,
                        append = append,
                        result = result,
                    )
                }
                .onFailure {
                    handleLoadPageFailure(
                        search = search,
                        append = append,
                    )
                }
        }
    }

    private fun preparePageLoad(append: Boolean) {
        if (append) {
            _uiState.update { current ->
                (current as? MealReadyListUiState.Ready)?.copy(isAppending = true) ?: current
            }
            return
        }

        currentItemsById = emptyMap()
        _uiState.update { current ->
            when (current) {
                MealReadyListUiState.Loading -> current
                is MealReadyListUiState.Ready -> current.copy(
                    meals = emptyList(),
                    selectedMealId = null,
                    isAppending = false,
                    canLoadMore = false,
                    currentPage = FIRST_PAGE,
                )
            }
        }
    }

    private suspend fun handleLoadPageSuccess(
        search: String,
        page: Int,
        append: Boolean,
        result: FoodCatalogSearchPage,
    ) = withContext(Dispatchers.Default) {
        if (shouldIgnoreSuccess(search = search, page = page, append = append)) return@withContext

        val newItems = result.items
            .distinctBy(FoodCatalogItem::stableId)

        currentItemsById = buildMap {
            if (append) putAll(currentItemsById)
            newItems.forEach { put(it.stableId(), it) }
        }

        val newMeals = newItems.map { item ->
            uiMapper.mapToReadyMealUi(
                item = item,
                id = item.stableId(),
                summary = item.toSummaryText(),
            )
        }

        _uiState.update { current ->
            val currentState = current as? MealReadyListUiState.Ready
                ?: MealReadyListUiState.Ready(meals = emptyList(), query = search)
            val mergedMeals = if (append) {
                (currentState.meals + newMeals).distinctBy(ReadyMealUi::id)
            } else {
                newMeals
            }
            currentState.copy(
                meals = mergedMeals,
                query = search,
                selectedMealId = currentState.selectedMealId.takeIf { selectedId ->
                    mergedMeals.any { it.id == selectedId }
                },
                currentPage = page,
                isAppending = false,
                canLoadMore = page + 1 < result.totalPages,
            )
        }
    }

    private fun handleLoadPageFailure(
        search: String,
        append: Boolean,
    ) {
        if (shouldIgnoreFailure(search = search, append = append)) return

        _uiState.update { current ->
            when (current) {
                MealReadyListUiState.Loading -> MealReadyListUiState.Ready(
                    meals = emptyList(),
                    query = search,
                )

                is MealReadyListUiState.Ready -> current.copy(
                    isAppending = false,
                    canLoadMore = false,
                    meals = if (append) current.meals else emptyList(),
                    currentPage = if (append) current.currentPage else FIRST_PAGE,
                )
            }
        }
    }

    private fun shouldIgnoreSuccess(
        search: String,
        page: Int,
        append: Boolean,
    ): Boolean {
        val latestState = _uiState.value as? MealReadyListUiState.Ready ?: return false
        if (!append) {
            return latestState.query != search
        }
        return latestState.query != search || latestState.currentPage >= page
    }

    private fun shouldIgnoreFailure(
        search: String,
        append: Boolean,
    ): Boolean {
        val latestState = _uiState.value as? MealReadyListUiState.Ready ?: return false
        if (!append) {
            return latestState.query != search
        }
        return latestState.query != search
    }

    private fun FoodCatalogItem.toSummaryText(): String =
        context.getString(
            R.string.meal_ready_summary,
            kcalPer100g.roundToInt(),
            proteinPer100g.formatNumber(),
            fatPer100g.formatNumber(),
            carbsPer100g.formatNumber(),
        )

    private fun Double.formatNumber(): String =
        if (this % 1.0 == 0.0) toInt().toString() else toString().trimEnd('0').trimEnd('.')

    private companion object {
        const val FIRST_PAGE = 0
        const val PAGE_SIZE = 20
    }
}

private fun FoodCatalogItem.stableId(): String = listOfNotNull(
    id?.toString(),
    barcode.takeIf { it.isNotBlank() },
    provider.takeIf { it.isNotBlank() },
    brand.takeIf { it.isNotBlank() },
    name,
).joinToString("|")
