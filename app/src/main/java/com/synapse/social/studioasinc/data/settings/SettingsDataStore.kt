package com.synapse.social.studioasinc.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Top-level property delegate for DataStore
private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "synapse_settings")

@Singleton
class SettingsDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    object PrefKeys {
        // Privacy
        val PRIVATE_ACCOUNT = booleanPreferencesKey("private_account")
        val PROFILE_VISIBILITY = stringPreferencesKey("profile_visibility")
        // Notifications
        val PUSH_NOTIFICATIONS = booleanPreferencesKey("push_notifications")
        val MENTIONS_NOTIFICATIONS = booleanPreferencesKey("mentions_notifications")
        val LIKES_NOTIFICATIONS = booleanPreferencesKey("likes_notifications")
        val COMMENTS_NOTIFICATIONS = booleanPreferencesKey("comments_notifications")
        val FOLLOWS_NOTIFICATIONS = booleanPreferencesKey("follows_notifications")
        // Appearance
        val THEME = stringPreferencesKey("theme")
        val FONT_SIZE = stringPreferencesKey("font_size")
        // Security
        val TWO_FACTOR_AUTH = booleanPreferencesKey("two_factor_auth")
        val APP_LOCK = booleanPreferencesKey("app_lock")
        // Connected Accounts
        val CONNECT_GOOGLE = booleanPreferencesKey("connect_google")
        val CONNECT_FACEBOOK = booleanPreferencesKey("connect_facebook")
        val CONNECT_TWITTER = booleanPreferencesKey("connect_twitter")
        // AI & API
        val AI_ASSISTANT = stringPreferencesKey("ai_assistant")
        val AI_MODEL = stringPreferencesKey("ai_model")
        val CONNECT_CLOUDINARY = booleanPreferencesKey("connect_cloudinary")
        val CONNECT_CLOUDFLARE = booleanPreferencesKey("connect_cloudflare")
        val CONNECT_GITHUB = booleanPreferencesKey("connect_github")
        val CONNECT_WITH_GOOGLE_API = booleanPreferencesKey("connect_with_google_api")
    }

    /**
     * Gets a Flow for a given preference key.
     * @param key The preference key to observe.
     * @param defaultValue The default value to return if the key is not set.
     */
    fun <T> getPreferenceFlow(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return context.settingsDataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    /**
     * Sets a value for a given preference key.
     * @param key The preference key to set.
     * @param value The value to write.
     */
    suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) {
        context.settingsDataStore.edit { settings ->
            settings[key] = value
        }
    }
}
