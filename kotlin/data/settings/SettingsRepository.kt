package data.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isPrivateAccount: Flow<Boolean>
    val themeMode: Flow<String>

    suspend fun setPrivateAccount(enabled: Boolean)
    suspend fun setThemeMode(mode: String)
}

