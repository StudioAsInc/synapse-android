package com.synapse.social.studioasinc.ui.settings

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapse.social.studioasinc.data.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // --- State Flows for UI to observe ---

    val theme: StateFlow<String> = settingsRepository.theme
        .stateIn(viewModelScope, SharingStarted.Eagerly, "system")

    val aiAssistant: StateFlow<String> = settingsRepository.aiAssistant
        .stateIn(viewModelScope, SharingStarted.Eagerly, "gemini")

    val privateAccount: StateFlow<Boolean> = settingsRepository.privateAccount
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // --- Functions to update settings ---

    fun setTheme(theme: String) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    fun setAiAssistant(assistant: String) {
        viewModelScope.launch {
            settingsRepository.setAiAssistant(assistant)
        }
    }

    fun setAiModel(model: String) {
        viewModelScope.launch {
            settingsRepository.setAiModel(model)
        }
    }

    fun setPrivateAccount(isPrivate: Boolean) {
        viewModelScope.launch {
            settingsRepository.setPrivateAccount(isPrivate)
        }
    }

    fun setBooleanPreference(key: Preferences.Key<Boolean>, value: Boolean) {
        viewModelScope.launch {
            settingsRepository.setBooleanPreference(key, value)
        }
    }
}
