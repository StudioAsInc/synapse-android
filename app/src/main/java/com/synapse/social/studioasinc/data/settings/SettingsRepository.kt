package com.synapse.social.studioasinc.data.settings

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the settings data layer.
 * This abstracts the data source (local or remote) from the ViewModels.
 */
interface SettingsRepository {

    // --- Appearance ---
    val theme: Flow<String>
    suspend fun setTheme(theme: String)

    val fontSize: Flow<String>
    suspend fun setFontSize(fontSize: String)

    // --- Privacy ---
    val privateAccount: Flow<Boolean>
    suspend fun setPrivateAccount(isPrivate: Boolean)

    val profileVisibility: Flow<String>
    suspend fun setProfileVisibility(visibility: String)

    // --- AI & API ---
    val aiAssistant: Flow<String>
    suspend fun setAiAssistant(assistant: String)

    val aiModel: Flow<String>
    suspend fun setAiModel(model: String)

    // --- Generic Accessors for simple preferences ---

    /**
     * Gets a Flow for any boolean preference.
     * @param key The preference key from [SettingsDataStore.PrefKeys].
     * @param defaultValue The default value.
     */
    fun getBooleanPreference(key: Preferences.Key<Boolean>, defaultValue: Boolean): Flow<Boolean>

    /**
     * Sets the value for any boolean preference.
     * @param key The preference key from [SettingsDataStore.PrefKeys].
     * @param value The new value.
     */
    suspend fun setBooleanPreference(key: Preferences.Key<Boolean>, value: Boolean)
}
