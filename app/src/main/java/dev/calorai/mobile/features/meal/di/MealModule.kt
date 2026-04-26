package dev.calorai.mobile.features.meal.di

import dev.calorai.mobile.core.network.di.NetworkContext
import dev.calorai.mobile.features.meal.data.api.MealApi
import dev.calorai.mobile.features.meal.data.mappers.MealMapper
import dev.calorai.mobile.features.meal.data.repository.MealRepositoryImpl
import dev.calorai.mobile.features.meal.details.di.mealDetailsModule
import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.usecases.CreateMealEntryUseCase
import dev.calorai.mobile.features.meal.domain.usecases.CreateMealEntryUseCaseImpl
import dev.calorai.mobile.features.meal.domain.usecases.DeleteMealEntryUseCase
import dev.calorai.mobile.features.meal.domain.usecases.DeleteMealEntryUseCaseImpl
import dev.calorai.mobile.features.meal.domain.usecases.DeleteMealUseCase
import dev.calorai.mobile.features.meal.domain.usecases.DeleteMealUseCaseImpl
import dev.calorai.mobile.features.meal.domain.usecases.GetAllMealEntriesUseCase
import dev.calorai.mobile.features.meal.domain.usecases.GetAllMealEntriesUseCaseImpl
import dev.calorai.mobile.features.meal.domain.usecases.GetMealEntryUseCase
import dev.calorai.mobile.features.meal.domain.usecases.GetMealEntryUseCaseImpl
import dev.calorai.mobile.features.meal.domain.usecases.RecognizeMealUseCase
import dev.calorai.mobile.features.meal.domain.usecases.RecognizeMealUseCaseImpl
import dev.calorai.mobile.features.meal.domain.usecases.UpdateMealEntryUseCase
import dev.calorai.mobile.features.meal.domain.usecases.UpdateMealEntryUseCaseImpl
import dev.calorai.mobile.features.meal.edit.manual.di.mealManualEditorModule
import dev.calorai.mobile.features.meal.ready.di.mealReadyListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

internal val mealModule = module {

    single { get<Retrofit>(qualifier<NetworkContext.Authorized>()).create(MealApi::class.java) }
    factory { MealMapper() }
    single<MealRepository> {
        MealRepositoryImpl(
            api = get(),
            dailyMealsDao = get(),
            ingredientsDao = get(),
            mapper = get(),
        )
    }
    factory<CreateMealEntryUseCase> {
        CreateMealEntryUseCaseImpl(repository = get())
    }
    factory<UpdateMealEntryUseCase> {
        UpdateMealEntryUseCaseImpl(repository = get())
    }
    factory<GetMealEntryUseCase> {
        GetMealEntryUseCaseImpl(repository = get())
    }
    factory<DeleteMealEntryUseCase> {
        DeleteMealEntryUseCaseImpl(repository = get())
    }
    factory<DeleteMealUseCase> {
        DeleteMealUseCaseImpl(repository = get())
    }
    factory<GetAllMealEntriesUseCase> {
        GetAllMealEntriesUseCaseImpl(repository = get())
    }
    factory<RecognizeMealUseCase> {
        RecognizeMealUseCaseImpl(
            context = androidContext(),
            repository = get(),
        )
    }
    includes(
        mealManualEditorModule,
        mealDetailsModule,
        mealReadyListModule,
    )
}
