package dev.calorai.mobile.features.profile.di

import dev.calorai.mobile.core.network.di.RETROFIT_AUTHORIZED
import dev.calorai.mobile.features.profile.data.ProfileMapper
import dev.calorai.mobile.features.profile.data.ProfileRepositoryImpl
import dev.calorai.mobile.features.profile.data.api.UserProfileApi
import dev.calorai.mobile.features.profile.domain.GetUserProfileUseCase
import dev.calorai.mobile.features.profile.domain.GetUserProfileUseCaseImpl
import dev.calorai.mobile.features.profile.domain.ProfileRepository
import dev.calorai.mobile.features.profile.domain.UpdateUserProfileUseCase
import dev.calorai.mobile.features.profile.domain.UpdateUserProfileUseCaseImpl
import dev.calorai.mobile.features.profile.ui.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

internal val profileModule = module {
    single { get<Retrofit>(named(RETROFIT_AUTHORIZED)).create(UserProfileApi::class.java) }
    factory { ProfileMapper(androidContext()) }
    single<ProfileRepository> {
        ProfileRepositoryImpl(
            userProfileApi = get(),
            userDao = get(),
            mapper = get(),
        )
    }
    factory<UpdateUserProfileUseCase> {
        UpdateUserProfileUseCaseImpl(repository = get())
    }
    factory<GetUserProfileUseCase> {
        GetUserProfileUseCaseImpl(repository = get())
    }
    viewModelOf(::ProfileViewModel)
}
