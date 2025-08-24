package data.settings

class SettingsRepositoryImpl(
    private val store: SettingsStore
) : SettingsRepository {

    override val isPrivateAccount = store.isPrivateAccount
    override val themeMode = store.themeMode

    override suspend fun setPrivateAccount(enabled: Boolean) {
        store.setPrivateAccount(enabled)
    }

    override suspend fun setThemeMode(mode: String) {
        store.setThemeMode(mode)
    }
}

