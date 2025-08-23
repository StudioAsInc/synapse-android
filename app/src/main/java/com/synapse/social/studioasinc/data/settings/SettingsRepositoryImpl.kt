package com.synapse.social.studioasinc.data.settings

import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: SettingsDataStore,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : SettingsRepository {

    private val userSettingsDoc
        get() = auth.currentUser?.uid?.let {
            firestore.collection("user_settings").document(it)
        }

    // --- Appearance ---
    override val theme: Flow<String> = dataStore.getPreferenceFlow(SettingsDataStore.PrefKeys.THEME, "system")
    override suspend fun setTheme(theme: String) {
        dataStore.setPreference(SettingsDataStore.PrefKeys.THEME, theme)
        updateFirestore(mapOf("theme" to theme))
    }

    override val fontSize: Flow<String> = dataStore.getPreferenceFlow(SettingsDataStore.PrefKeys.FONT_SIZE, "normal")
    override suspend fun setFontSize(fontSize: String) {
        dataStore.setPreference(SettingsDataStore.PrefKeys.FONT_SIZE, fontSize)
        updateFirestore(mapOf("font_size" to fontSize))
    }

    // --- Privacy ---
    override val privateAccount: Flow<Boolean> = dataStore.getPreferenceFlow(SettingsDataStore.PrefKeys.PRIVATE_ACCOUNT, false)
    override suspend fun setPrivateAccount(isPrivate: Boolean) {
        setBooleanPreference(SettingsDataStore.PrefKeys.PRIVATE_ACCOUNT, isPrivate)
    }

    override val profileVisibility: Flow<String> = dataStore.getPreferenceFlow(SettingsDataStore.PrefKeys.PROFILE_VISIBILITY, "public")
    override suspend fun setProfileVisibility(visibility: String) {
        dataStore.setPreference(SettingsDataStore.PrefKeys.PROFILE_VISIBILITY, visibility)
        updateFirestore(mapOf("profile_visibility" to visibility))
    }

    // --- AI & API ---
    override val aiAssistant: Flow<String> = dataStore.getPreferenceFlow(SettingsDataStore.PrefKeys.AI_ASSISTANT, "gemini")
    override suspend fun setAiAssistant(assistant: String) {
        dataStore.setPreference(SettingsDataStore.PrefKeys.AI_ASSISTANT, assistant)
        updateFirestore(mapOf("ai_assistant" to assistant))
    }

    override val aiModel: Flow<String> = dataStore.getPreferenceFlow(SettingsDataStore.PrefKeys.AI_MODEL, "default")
    override suspend fun setAiModel(model: String) {
        dataStore.setPreference(SettingsDataStore.PrefKeys.AI_MODEL, model)
        updateFirestore(mapOf("ai_model" to model))
    }

    // --- Generic Accessors ---
    override fun getBooleanPreference(key: Preferences.Key<Boolean>, defaultValue: Boolean): Flow<Boolean> {
        return dataStore.getPreferenceFlow(key, defaultValue)
    }

    override suspend fun setBooleanPreference(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.setPreference(key, value)
        updateFirestore(mapOf(key.name to value))
    }

    /**
     * Helper function to update Firestore with error handling.
     */
    private suspend fun updateFirestore(data: Map<String, Any>) {
        try {
            userSettingsDoc?.set(data, SetOptions.merge())?.await()
        } catch (e: Exception) {
            // In a real app, you might want to log this to a crash reporting tool
            // or queue the update for later. For now, we'll just log it.
             android.util.Log.e("SettingsRepository", "Failed to update Firestore", e)
        }
    }
}
