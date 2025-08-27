package com.synapse.social.studioasinc.groupchat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.synapse.social.studioasinc.groupchat.data.model.Group
import com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository
import com.synapse.social.studioasinc.groupchat.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InboxUiState())
    val uiState: StateFlow<InboxUiState> = _uiState.asStateFlow()

    val groupsPagingData: Flow<PagingData<Group>> = groupRepository
        .getAllGroupsPaged()
        .cachedIn(viewModelScope)

    val recentGroups: Flow<List<Group>> = groupRepository
        .getAllGroupsFlow()
        .map { groups -> groups.take(10) } // Show recent 10 groups

    init {
        loadInitialData()
        startPeriodicSync()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // Sync user data
                userRepository.syncUserData()
                
                // Sync groups
                groupRepository.syncGroups()
                
                // Update online status
                userRepository.updateOnlineStatus(true)
                
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load data"
                    )
                }
            }
        }
    }

    fun searchGroups(query: String) {
        if (query.isEmpty()) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true) }
            
            groupRepository.searchGroups(query).fold(
                onSuccess = { groups ->
                    _uiState.update { 
                        it.copy(
                            searchResults = groups,
                            isSearching = false,
                            error = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            isSearching = false,
                            error = exception.message ?: "Search failed"
                        )
                    }
                }
            )
        }
    }

    fun clearSearch() {
        _uiState.update { 
            it.copy(
                searchResults = emptyList(),
                isSearching = false
            )
        }
    }

    fun refreshGroups() {
        viewModelScope.launch {
            try {
                groupRepository.syncGroups()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to refresh groups")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun startPeriodicSync() {
        viewModelScope.launch {
            // Sync every 5 minutes
            kotlinx.coroutines.delay(5 * 60 * 1000)
            while (true) {
                try {
                    groupRepository.syncGroups()
                    kotlinx.coroutines.delay(5 * 60 * 1000)
                } catch (e: Exception) {
                    kotlinx.coroutines.delay(30 * 1000) // Retry in 30 seconds on error
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Update offline status when ViewModel is cleared
        viewModelScope.launch {
            userRepository.updateOnlineStatus(false)
        }
    }
}

data class InboxUiState(
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val searchResults: List<Group> = emptyList(),
    val error: String? = null
)