package data.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings_prefs")

object SettingsKeys {
    val PRIVATE_ACCOUNT = booleanPreferencesKey("privacy_private_account")
    val PUSH = booleanPreferencesKey("notif_enabled")
    val MENTIONS = booleanPreferencesKey("notif_mentions")
    val LIKES = booleanPreferencesKey("notif_likes")
    val COMMENTS = booleanPreferencesKey("notif_comments")
    val FOLLOWS = booleanPreferencesKey("notif_follows")

    val THEME = stringPreferencesKey("appearance_theme") // system|light|dark
    val FONT = stringPreferencesKey("appearance_font_size") // small|normal|large
    val PROFILE_VIS = stringPreferencesKey("privacy_profile_visibility") // public|followers
}

class SettingsStore(private val context: Context) {

    val isPrivateAccount: Flow<Boolean> = context.settingsDataStore.data.map { it[SettingsKeys.PRIVATE_ACCOUNT] ?: false }
    val isPushEnabled: Flow<Boolean> = context.settingsDataStore.data.map { it[SettingsKeys.PUSH] ?: true }
    val isMentionsEnabled: Flow<Boolean> = context.settingsDataStore.data.map { it[SettingsKeys.MENTIONS] ?: true }
    val isLikesEnabled: Flow<Boolean> = context.settingsDataStore.data.map { it[SettingsKeys.LIKES] ?: true }
    val isCommentsEnabled: Flow<Boolean> = context.settingsDataStore.data.map { it[SettingsKeys.COMMENTS] ?: true }
    val isFollowsEnabled: Flow<Boolean> = context.settingsDataStore.data.map { it[SettingsKeys.FOLLOWS] ?: true }

    val themeMode: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.THEME] ?: "system" }
    val fontSize: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.FONT] ?: "normal" }
    val profileVisibility: Flow<String> = context.settingsDataStore.data.map { it[SettingsKeys.PROFILE_VIS] ?: "public" }

    suspend fun setPrivateAccount(value: Boolean) = set(SettingsKeys.PRIVATE_ACCOUNT, value)
    suspend fun setPushEnabled(value: Boolean) = set(SettingsKeys.PUSH, value)
    suspend fun setMentionsEnabled(value: Boolean) = set(SettingsKeys.MENTIONS, value)
    suspend fun setLikesEnabled(value: Boolean) = set(SettingsKeys.LIKES, value)
    suspend fun setCommentsEnabled(value: Boolean) = set(SettingsKeys.COMMENTS, value)
    suspend fun setFollowsEnabled(value: Boolean) = set(SettingsKeys.FOLLOWS, value)
    suspend fun setThemeMode(value: String) = set(SettingsKeys.THEME, value)
    suspend fun setFontSize(value: String) = set(SettingsKeys.FONT, value)
    suspend fun setProfileVisibility(value: String) = set(SettingsKeys.PROFILE_VIS, value)

    private suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        context.settingsDataStore.edit { prefs ->
            prefs[key] = value
        }
    }
}

