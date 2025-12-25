package dev.calorai.mobile.features.profile.di

import dev.calorai.mobile.core.network.di.NetworkContext
import dev.calorai.mobile.features.profile.data.ProfileMapper
import dev.calorai.mobile.features.profile.data.ProfileRepositoryImpl
import dev.calorai.mobile.features.profile.data.api.UserProfileApi
import dev.calorai.mobile.features.profile.domain.GetUserProfileUseCase
import dev.calorai.mobile.features.profile.domain.GetUserProfileUseCaseImpl
import dev.calorai.mobile.features.profile.domain.ProfileRepository
import dev.calorai.mobile.features.profile.domain.UpdateUserProfileUseCase
import dev.calorai.mobile.features.profile.domain.UpdateUserProfileUseCaseImpl
import dev.calorai.mobile.features.profile.domain.UserIdStore
import dev.calorai.mobile.features.profile.domain.UserIdStoreImpl
import dev.calorai.mobile.features.profile.ui.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

internal val profileModule = module {
    single { get<Retrofit>(qualifier<NetworkContext.Authorized>()).create(UserProfileApi::class.java) }
    factory { ProfileMapper(androidContext()) }
    single<ProfileRepository> {
        ProfileRepositoryImpl(
            userProfileApi = get(),
            userDao = get(),
            mapper = get(),
        )
    }
    single<UserIdStore> {
        UserIdStoreImpl(
            context = androidContext(),
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
