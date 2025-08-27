package com.synapse.social.studioasinc.groupchat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapse.social.studioasinc.groupchat.data.model.*
import com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository
import com.synapse.social.studioasinc.groupchat.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateGroupUiState())
    val uiState: StateFlow<CreateGroupUiState> = _uiState.asStateFlow()

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults.asStateFlow()

    fun updateGroupName(name: String) {
        _uiState.update { it.copy(groupName = name) }
        validateForm()
    }

    fun updateGroupDescription(description: String) {
        _uiState.update { it.copy(groupDescription = description) }
    }

    fun updateGroupIcon(iconFile: File?) {
        _uiState.update { it.copy(groupIconFile = iconFile) }
    }

    fun updateIsPrivate(isPrivate: Boolean) {
        _uiState.update { it.copy(isPrivate = isPrivate) }
    }

    fun updateMaxMembers(maxMembers: Int) {
        _uiState.update { it.copy(maxMembers = maxMembers) }
        validateForm()
    }

    fun searchUsers(query: String) {
        if (query.isEmpty()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSearchingUsers = true) }
            
            userRepository.searchUsers(query).fold(
                onSuccess = { users ->
                    // Filter out already selected members
                    val filteredUsers = users.filter { user ->
                        _uiState.value.selectedMembers.none { it.userId == user.id }
                    }
                    _searchResults.value = filteredUsers
                    _uiState.update { it.copy(isSearchingUsers = false) }
                },
                onFailure = { exception ->
                    _uiState.update { 
                        it.copy(
                            isSearchingUsers = false,
                            error = exception.message ?: "Failed to search users"
                        )
                    }
                }
            )
        }
    }

    fun addMember(user: User, role: UserRole = UserRole.MEMBER) {
        val currentState = _uiState.value
        val newMember = SelectedMember(
            userId = user.id,
            userName = user.getDisplayNameOrUsername(),
            userPhotoUrl = user.photoUrl,
            role = role
        )
        
        if (currentState.selectedMembers.none { it.userId == user.id }) {
            _uiState.update { 
                it.copy(selectedMembers = it.selectedMembers + newMember)
            }
            validateForm()
        }
    }

    fun removeMember(userId: String) {
        _uiState.update { 
            it.copy(selectedMembers = it.selectedMembers.filter { member -> member.userId != userId })
        }
        validateForm()
    }

    fun updateMemberRole(userId: String, newRole: UserRole) {
        _uiState.update { state ->
            state.copy(
                selectedMembers = state.selectedMembers.map { member ->
                    if (member.userId == userId) {
                        member.copy(role = newRole)
                    } else {
                        member
                    }
                }
            )
        }
    }

    fun createGroup() {
        val currentState = _uiState.value
        if (!currentState.isFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true, error = null) }
            
            try {
                val group = Group(
                    name = currentState.groupName.trim(),
                    description = currentState.groupDescription.trim(),
                    isPrivate = currentState.isPrivate,
                    maxMembers = currentState.maxMembers,
                    settings = GroupSettings(
                        allowMembersToAddOthers = currentState.allowMembersToAddOthers,
                        allowMembersToEditInfo = currentState.allowMembersToEditInfo,
                        onlyAdminsCanMessage = currentState.onlyAdminsCanMessage
                    )
                )

                val result = groupRepository.createGroup(group, currentState.groupIconFile)
                
                result.fold(
                    onSuccess = { groupId ->
                        // Add selected members to the group
                        addMembersToGroup(groupId, currentState.selectedMembers)
                    },
                    onFailure = { exception ->
                        _uiState.update { 
                            it.copy(
                                isCreating = false,
                                error = exception.message ?: "Failed to create group"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isCreating = false,
                        error = e.message ?: "Failed to create group"
                    )
                }
            }
        }
    }

    private suspend fun addMembersToGroup(groupId: String, members: List<SelectedMember>) {
        try {
            val currentUser = userRepository.getCurrentUser().getOrNull()
            if (currentUser == null) {
                _uiState.update { 
                    it.copy(
                        isCreating = false,
                        error = "User not authenticated"
                    )
                }
                return
            }

            var memberAddedSuccessfully = true
            
            members.forEach { selectedMember ->
                val user = userRepository.getUserById(selectedMember.userId).getOrNull()
                if (user != null) {
                    val result = groupRepository.addMember(
                        groupId = groupId,
                        user = user,
                        role = selectedMember.role.name,
                        addedBy = currentUser.id
                    )
                    
                    if (result.isFailure) {
                        memberAddedSuccessfully = false
                    }
                }
            }

            _uiState.update { 
                it.copy(
                    isCreating = false,
                    isGroupCreated = true,
                    createdGroupId = groupId,
                    error = if (!memberAddedSuccessfully) "Group created but some members couldn't be added" else null
                )
            }
        } catch (e: Exception) {
            _uiState.update { 
                it.copy(
                    isCreating = false,
                    error = "Group created but failed to add members: ${e.message}"
                )
            }
        }
    }

    fun toggleGroupSetting(setting: String) {
        _uiState.update { state ->
            when (setting) {
                "allowMembersToAddOthers" -> state.copy(allowMembersToAddOthers = !state.allowMembersToAddOthers)
                "allowMembersToEditInfo" -> state.copy(allowMembersToEditInfo = !state.allowMembersToEditInfo)
                "onlyAdminsCanMessage" -> state.copy(onlyAdminsCanMessage = !state.onlyAdminsCanMessage)
                else -> state
            }
        }
    }

    private fun validateForm() {
        _uiState.update { state ->
            val isValid = state.groupName.trim().isNotEmpty() &&
                    state.groupName.trim().length <= 100 &&
                    state.maxMembers > 0 &&
                    state.maxMembers <= 256
            state.copy(isFormValid = isValid)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetCreationState() {
        _uiState.update { 
            it.copy(
                isGroupCreated = false,
                createdGroupId = null
            )
        }
    }
}

data class CreateGroupUiState(
    val groupName: String = "",
    val groupDescription: String = "",
    val groupIconFile: File? = null,
    val isPrivate: Boolean = false,
    val maxMembers: Int = 256,
    val selectedMembers: List<SelectedMember> = emptyList(),
    val allowMembersToAddOthers: Boolean = false,
    val allowMembersToEditInfo: Boolean = false,
    val onlyAdminsCanMessage: Boolean = false,
    val isSearchingUsers: Boolean = false,
    val isCreating: Boolean = false,
    val isFormValid: Boolean = false,
    val isGroupCreated: Boolean = false,
    val createdGroupId: String? = null,
    val error: String? = null
)

data class SelectedMember(
    val userId: String,
    val userName: String,
    val userPhotoUrl: String,
    val role: UserRole
)