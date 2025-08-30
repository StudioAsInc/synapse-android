package com.synapse.social.studioasinc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatSettingsUiState(
    val username: String = "",
    val userAvatarUrl: String? = null,
    val isBlocked: Boolean = false,
    val readReceiptsEnabled: Boolean = true,
    val disappearingMessagesEnabled: Boolean = false,
    val disappearingMessagesValue: String = "Off",
    val autoSaveMediaEnabled: Boolean = false,
    val theme: String = "Default",
    val nickname: String = "",
    val wordEffect: String = "No word effect",
    val quickReaction: String = "👍",
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class ChatSettingsViewModel(
    private val repository: ChatSettingsRepository = ChatSettingsRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatSettingsUiState())
    val uiState: StateFlow<ChatSettingsUiState> = _uiState.asStateFlow()
    private var currentUserId: String? = null

    fun loadUserSettings(userId: String) {
        currentUserId = userId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val user = repository.getUser(userId)
                val isBlocked = repository.isUserBlocked(userId)
                val settings = repository.getChatSettings(userId)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        username = user?.username ?: "",
                        nickname = if (user?.nickname != "null" && user?.nickname?.isNotEmpty() == true) user.nickname else "@${user?.username}",
                        userAvatarUrl = if (user?.avatar != "null") user?.avatar else null,
                        isBlocked = isBlocked,
                        readReceiptsEnabled = settings?.readReceiptsEnabled ?: true,
                        disappearingMessagesEnabled = settings?.disappearingMessagesEnabled ?: false,
                        autoSaveMediaEnabled = settings?.autoSaveMediaEnabled ?: false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun blockUser(userId: String) {
        viewModelScope.launch {
            try {
                repository.blockUser(userId)
                _uiState.update { it.copy(isBlocked = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message)
                }
            }
        }
    }

    fun onReadReceiptsChanged(isEnabled: Boolean) {
        _uiState.update { it.copy(readReceiptsEnabled = isEnabled) }
        viewModelScope.launch {
            currentUserId?.let {
                repository.saveReadReceiptsSetting(it, isEnabled)
            }
        }
    }

    fun onDisappearingMessagesChanged(isEnabled: Boolean) {
        _uiState.update { it.copy(disappearingMessagesEnabled = isEnabled) }
        viewModelScope.launch {
            currentUserId?.let {
                repository.saveDisappearingMessagesSetting(it, isEnabled)
            }
        }
    }

    fun onAutoSaveMediaChanged(isEnabled: Boolean) {
        _uiState.update { it.copy(autoSaveMediaEnabled = isEnabled) }
        viewModelScope.launch {
            currentUserId?.let {
                repository.saveAutoSaveMediaSetting(it, isEnabled)
            }
        }
    }
}
