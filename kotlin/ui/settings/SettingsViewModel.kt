package ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import data.settings.SettingsRepository
import data.settings.SettingsRepositoryImpl
import data.settings.SettingsStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SettingsRepository = SettingsRepositoryImpl(SettingsStore(application))

    private val _privateAccount = MutableStateFlow(false)
    val privateAccount: StateFlow<Boolean> = _privateAccount

    private val _themeMode = MutableStateFlow("system")
    val themeMode: StateFlow<String> = _themeMode

    init {
        viewModelScope.launch {
            repository.isPrivateAccount.collectLatest { _privateAccount.value = it }
        }
        viewModelScope.launch {
            repository.themeMode.collectLatest { _themeMode.value = it }
        }
    }

    fun setPrivateAccount(enabled: Boolean) {
        viewModelScope.launch { repository.setPrivateAccount(enabled) }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch { repository.setThemeMode(mode) }
    }
}

