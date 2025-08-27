package com.synapse.social.studioasinc.groupchat.data.repository

import androidx.paging.PagingData
import com.synapse.social.studioasinc.groupchat.data.model.Group
import com.synapse.social.studioasinc.groupchat.data.model.GroupMember
import com.synapse.social.studioasinc.groupchat.data.model.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface GroupRepository {
    
    // Group operations
    fun getAllGroupsPaged(): Flow<PagingData<Group>>
    fun getAllGroupsFlow(): Flow<List<Group>>
    suspend fun getGroupById(groupId: String): Result<Group>
    fun getGroupByIdFlow(groupId: String): Flow<Group?>
    suspend fun createGroup(group: Group, iconFile: File?): Result<String>
    suspend fun updateGroup(group: Group): Result<Unit>
    suspend fun deleteGroup(groupId: String): Result<Unit>
    suspend fun searchGroups(query: String): Result<List<Group>>
    
    // Group membership operations
    suspend fun addMember(groupId: String, user: User, role: String, addedBy: String): Result<Unit>
    suspend fun removeMember(groupId: String, userId: String): Result<Unit>
    suspend fun updateMemberRole(groupId: String, userId: String, newRole: String): Result<Unit>
    suspend fun getGroupMembers(groupId: String): Result<List<GroupMember>>
    fun getGroupMembersFlow(groupId: String): Flow<List<GroupMember>>
    suspend fun getGroupMember(groupId: String, userId: String): Result<GroupMember?>
    suspend fun searchGroupMembers(groupId: String, query: String): Result<List<GroupMember>>
    
    // Group icon operations
    suspend fun uploadGroupIcon(groupId: String, iconFile: File): Result<String>
    suspend fun deleteGroupIcon(groupId: String): Result<Unit>
    
    // Utility operations
    suspend fun getUserGroups(userId: String): Result<List<Group>>
    suspend fun isUserMember(groupId: String, userId: String): Result<Boolean>
    suspend fun getGroupMemberCount(groupId: String): Result<Int>
    suspend fun canUserPerformAction(groupId: String, userId: String, action: String): Result<Boolean>
    
    // Offline sync
    suspend fun syncGroups(): Result<Unit>
    suspend fun syncGroupMembers(groupId: String): Result<Unit>
}