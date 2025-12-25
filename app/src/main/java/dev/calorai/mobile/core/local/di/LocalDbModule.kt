package dev.calorai.mobile.core.local.di

import androidx.room.Room
import dev.calorai.mobile.core.local.db.CalorAiDatabase
import dev.calorai.mobile.core.local.deviceId.DeviceIdStore
import dev.calorai.mobile.core.local.deviceId.DeviceIdStoreImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CalorAiDatabase::class.java,
            "calorai_db"
        ).build()
    }

    single { get<CalorAiDatabase>().userDao() }
    single { get<CalorAiDatabase>().dailyMealDao() }
    single<DeviceIdStore> { DeviceIdStoreImpl(context = androidContext()) }
}
