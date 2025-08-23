package com.synapse.social.studioasinc.di

import com.synapse.social.studioasinc.data.settings.JavaSettingsRepository
import com.synapse.social.studioasinc.data.settings.JavaSettingsRepositoryImpl
import com.synapse.social.studioasinc.data.settings.SettingsRepository
import com.synapse.social.studioasinc.data.settings.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindJavaSettingsRepository(
        impl: JavaSettingsRepositoryImpl
    ): JavaSettingsRepository
}
