package di

import android.content.Context
import data.settings.SettingsRepository
import data.settings.SettingsRepositoryImpl
import data.settings.SettingsStore

// Hilt DI stub module placeholder. Replace with @Module and @Provides as needed.
object SettingsModule {
    fun provideSettingsRepository(context: Context): SettingsRepository =
        SettingsRepositoryImpl(SettingsStore(context))
}

