package com.synapse.social.studioasinc.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.synapse.social.studioasinc.data.settings.JavaSettingsRepository
import com.synapse.social.studioasinc.data.settings.JavaSettingsRepositoryImpl
import com.synapse.social.studioasinc.data.settings.SettingsRepository
import com.synapse.social.studioasinc.data.settings.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Module for providing concrete instances of dependencies like Firebase, SharedPreferences, etc.
@Module
@InstallIn(SingletonComponent::class)
object SettingsProvidesModule {

    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}

// Module for binding interfaces to their concrete implementations.
@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsBindingsModule {

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
