package com.synapse.social.studioasinc.groupchat.data.repository.impl

import androidx.paging.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao
import com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao
import com.synapse.social.studioasinc.groupchat.data.model.*
import com.synapse.social.studioasinc.groupchat.data.remote.FirebaseConstants
import com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao,
    private val groupMemberDao: GroupMemberDao,
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
) : GroupRepository {

    private val groupsRef = firebaseDatabase.getReference(FirebaseConstants.GROUPS)
    private val groupMembersRef = firebaseDatabase.getReference(FirebaseConstants.GROUP_MEMBERS)
    private val usersRef = firebaseDatabase.getReference(FirebaseConstants.USERS)
    private val userGroupsRef = firebaseDatabase.getReference(FirebaseConstants.USER_GROUPS)
    private val groupsByMemberRef = firebaseDatabase.getReference(FirebaseConstants.GROUPS_BY_MEMBER)
    private val membersByGroupRef = firebaseDatabase.getReference(FirebaseConstants.MEMBERS_BY_GROUP)

    override fun getAllGroupsPaged(): Flow<PagingData<Group>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { groupDao.getAllGroupsPaged() }
        ).flow
    }

    override fun getAllGroupsFlow(): Flow<List<Group>> {
        return groupDao.getAllGroupsFlow()
    }

    override suspend fun getGroupById(groupId: String): Result<Group> {
        return try {
            // Try local first
            val localGroup = groupDao.getGroupById(groupId)
            if (localGroup != null) {
                return Result.success(localGroup)
            }

            // Fetch from Firebase
            val snapshot = groupsRef.child(groupId).get().await()
            if (snapshot.exists()) {
                val group = snapshot.getValue(Group::class.java)
                if (group != null) {
                    groupDao.insertGroup(group)
                    Result.success(group)
                } else {
                    Result.failure(Exception("Failed to parse group data"))
                }
            } else {
                Result.failure(Exception("Group not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getGroupByIdFlow(groupId: String): Flow<Group?> {
        return groupDao.getGroupByIdFlow(groupId)
    }

    override suspend fun createGroup(group: Group, iconFile: File?): Result<String> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val groupId = groupsRef.push().key 
                ?: return Result.failure(Exception("Failed to generate group ID"))

            var iconUrl = ""
            if (iconFile != null) {
                val uploadResult = uploadGroupIcon(groupId, iconFile)
                if (uploadResult.isSuccess) {
                    iconUrl = uploadResult.getOrThrow()
                }
            }

            val newGroup = group.copy(
                id = groupId,
                iconUrl = iconUrl,
                createdBy = currentUser.uid,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                memberCount = 1
            )

            // Create group owner member
            val ownerMember = GroupMember(
                id = "${groupId}_${currentUser.uid}",
                groupId = groupId,
                userId = currentUser.uid,
                userName = currentUser.displayName ?: "",
                userPhotoUrl = currentUser.photoUrl?.toString() ?: "",
                role = UserRole.OWNER.name,
                joinedAt = System.currentTimeMillis(),
                addedBy = currentUser.uid
            )

            // Firebase batch operations
            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.GROUPS}/$groupId" to newGroup,
                "/${FirebaseConstants.GROUP_MEMBERS}/$groupId/${currentUser.uid}" to ownerMember,
                "/${FirebaseConstants.USER_GROUPS}/${currentUser.uid}/$groupId" to true,
                "/${FirebaseConstants.GROUPS_BY_MEMBER}/${currentUser.uid}/$groupId" to true,
                "/${FirebaseConstants.MEMBERS_BY_GROUP}/$groupId/${currentUser.uid}" to UserRole.OWNER.name
            )

            firebaseDatabase.reference.updateChildren(updates).await()

            // Save to local database
            groupDao.insertGroup(newGroup)
            groupMemberDao.insertGroupMember(ownerMember)

            Result.success(groupId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateGroup(group: Group): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            // Check if user has permission to edit
            val member = groupMemberDao.getGroupMember(group.id, currentUser.uid)
            if (member == null || !member.getUserRole().canEditGroupInfo()) {
                return Result.failure(Exception("Insufficient permissions"))
            }

            val updatedGroup = group.copy(updatedAt = System.currentTimeMillis())
            
            groupsRef.child(group.id).setValue(updatedGroup).await()
            groupDao.updateGroup(updatedGroup)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteGroup(groupId: String): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            // Check if user is owner
            val member = groupMemberDao.getGroupMember(groupId, currentUser.uid)
            if (member?.getUserRole() != UserRole.OWNER) {
                return Result.failure(Exception("Only group owner can delete the group"))
            }

            // Get all members to clean up user associations
            val members = groupMemberDao.getGroupMembers(groupId)
            
            val updates = hashMapOf<String, Any?>(
                "/${FirebaseConstants.GROUPS}/$groupId" to null,
                "/${FirebaseConstants.GROUP_MEMBERS}/$groupId" to null,
                "/${FirebaseConstants.GROUP_MESSAGES}/$groupId" to null,
                "/${FirebaseConstants.MEMBERS_BY_GROUP}/$groupId" to null
            )

            // Remove from user groups and groups by member
            members.forEach { member ->
                updates["/${FirebaseConstants.USER_GROUPS}/${member.userId}/$groupId"] = null
                updates["/${FirebaseConstants.GROUPS_BY_MEMBER}/${member.userId}/$groupId"] = null
            }

            firebaseDatabase.reference.updateChildren(updates).await()

            // Delete from local database
            groupDao.deleteGroup(groupId)
            groupMemberDao.deleteAllGroupMembers(groupId)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchGroups(query: String): Result<List<Group>> {
        return try {
            val localResults = groupDao.searchGroups(query)
            Result.success(localResults)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addMember(groupId: String, user: User, role: String, addedBy: String): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            // Check permissions
            val adderMember = groupMemberDao.getGroupMember(groupId, addedBy)
            if (adderMember == null || !adderMember.getUserRole().canManageMembers()) {
                return Result.failure(Exception("Insufficient permissions"))
            }

            // Check if user is already a member
            val existingMember = groupMemberDao.getGroupMember(groupId, user.id)
            if (existingMember != null && existingMember.isActive) {
                return Result.failure(Exception("User is already a member"))
            }

            // Check group member limit
            val group = groupDao.getGroupById(groupId)
                ?: return Result.failure(Exception("Group not found"))
            
            if (!group.canAddMoreMembers()) {
                return Result.failure(Exception("Group member limit reached"))
            }

            val newMember = GroupMember(
                id = "${groupId}_${user.id}",
                groupId = groupId,
                userId = user.id,
                userName = user.getDisplayNameOrUsername(),
                userPhotoUrl = user.photoUrl,
                role = role,
                joinedAt = System.currentTimeMillis(),
                addedBy = addedBy
            )

            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.GROUP_MEMBERS}/$groupId/${user.id}" to newMember,
                "/${FirebaseConstants.USER_GROUPS}/${user.id}/$groupId" to true,
                "/${FirebaseConstants.GROUPS_BY_MEMBER}/${user.id}/$groupId" to true,
                "/${FirebaseConstants.MEMBERS_BY_GROUP}/$groupId/${user.id}" to role,
                "/${FirebaseConstants.GROUPS}/$groupId/memberCount" to ServerValue.increment(1)
            )

            firebaseDatabase.reference.updateChildren(updates).await()

            // Update local database
            groupMemberDao.insertGroupMember(newMember)
            groupDao.updateMemberCount(groupId, group.memberCount + 1)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeMember(groupId: String, userId: String): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            // Check permissions
            val removerMember = groupMemberDao.getGroupMember(groupId, currentUser.uid)
            val targetMember = groupMemberDao.getGroupMember(groupId, userId)
            
            if (removerMember == null || targetMember == null) {
                return Result.failure(Exception("Member not found"))
            }

            // Can't remove yourself unless you're the owner leaving
            if (currentUser.uid == userId && removerMember.getUserRole() != UserRole.OWNER) {
                return Result.failure(Exception("Cannot remove yourself"))
            }

            // Check if remover has permission
            if (!removerMember.getUserRole().canRemoveMembers() && currentUser.uid != userId) {
                return Result.failure(Exception("Insufficient permissions"))
            }

            // Can't remove owner
            if (targetMember.getUserRole() == UserRole.OWNER && currentUser.uid != userId) {
                return Result.failure(Exception("Cannot remove group owner"))
            }

            val updates = hashMapOf<String, Any?>(
                "/${FirebaseConstants.GROUP_MEMBERS}/$groupId/$userId" to null,
                "/${FirebaseConstants.USER_GROUPS}/$userId/$groupId" to null,
                "/${FirebaseConstants.GROUPS_BY_MEMBER}/$userId/$groupId" to null,
                "/${FirebaseConstants.MEMBERS_BY_GROUP}/$groupId/$userId" to null,
                "/${FirebaseConstants.GROUPS}/$groupId/memberCount" to ServerValue.increment(-1)
            )

            firebaseDatabase.reference.updateChildren(updates).await()

            // Update local database
            groupMemberDao.removeMember(groupId, userId)
            val group = groupDao.getGroupById(groupId)
            if (group != null) {
                groupDao.updateMemberCount(groupId, group.memberCount - 1)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMemberRole(groupId: String, userId: String, newRole: String): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val updaterMember = groupMemberDao.getGroupMember(groupId, currentUser.uid)
            val targetMember = groupMemberDao.getGroupMember(groupId, userId)
            
            if (updaterMember == null || targetMember == null) {
                return Result.failure(Exception("Member not found"))
            }

            val newUserRole = UserRole.fromString(newRole)
            
            // Check if updater has permission to promote/demote
            if (!updaterMember.getUserRole().canPromoteMembers()) {
                return Result.failure(Exception("Insufficient permissions"))
            }

            // Can't change owner role
            if (targetMember.getUserRole() == UserRole.OWNER || newUserRole == UserRole.OWNER) {
                return Result.failure(Exception("Cannot change owner role"))
            }

            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.GROUP_MEMBERS}/$groupId/$userId/role" to newRole,
                "/${FirebaseConstants.MEMBERS_BY_GROUP}/$groupId/$userId" to newRole
            )

            firebaseDatabase.reference.updateChildren(updates).await()
            groupMemberDao.updateMemberRole(groupId, userId, newRole)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroupMembers(groupId: String): Result<List<GroupMember>> {
        return try {
            val localMembers = groupMemberDao.getGroupMembers(groupId)
            if (localMembers.isNotEmpty()) {
                return Result.success(localMembers)
            }

            // Fetch from Firebase
            val snapshot = groupMembersRef.child(groupId).get().await()
            val members = mutableListOf<GroupMember>()
            
            snapshot.children.forEach { memberSnapshot ->
                val member = memberSnapshot.getValue(GroupMember::class.java)
                if (member != null) {
                    members.add(member)
                }
            }
            
            if (members.isNotEmpty()) {
                groupMemberDao.insertGroupMembers(members)
            }
            
            Result.success(members)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getGroupMembersFlow(groupId: String): Flow<List<GroupMember>> {
        return groupMemberDao.getGroupMembersFlow(groupId)
    }

    override suspend fun getGroupMember(groupId: String, userId: String): Result<GroupMember?> {
        return try {
            val member = groupMemberDao.getGroupMember(groupId, userId)
            Result.success(member)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchGroupMembers(groupId: String, query: String): Result<List<GroupMember>> {
        return try {
            val members = groupMemberDao.searchGroupMembers(groupId, query)
            Result.success(members)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadGroupIcon(groupId: String, iconFile: File): Result<String> {
        return try {
            val storageRef = firebaseStorage.reference
                .child(FirebaseConstants.GROUP_ICONS)
                .child("$groupId.jpg")
            
            val uploadTask = storageRef.putFile(android.net.Uri.fromFile(iconFile)).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteGroupIcon(groupId: String): Result<Unit> {
        return try {
            val storageRef = firebaseStorage.reference
                .child(FirebaseConstants.GROUP_ICONS)
                .child("$groupId.jpg")
            
            storageRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserGroups(userId: String): Result<List<Group>> {
        return try {
            // Get user's group IDs from local database first
            val localGroups = groupDao.getAllGroups()
            val userGroups = localGroups.filter { group ->
                // Check if user is member from local data
                val member = groupMemberDao.getGroupMember(group.id, userId)
                member != null && member.isActive
            }
            
            Result.success(userGroups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isUserMember(groupId: String, userId: String): Result<Boolean> {
        return try {
            val member = groupMemberDao.getGroupMember(groupId, userId)
            Result.success(member != null && member.isActive)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGroupMemberCount(groupId: String): Result<Int> {
        return try {
            val count = groupMemberDao.getGroupMemberCount(groupId)
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun canUserPerformAction(groupId: String, userId: String, action: String): Result<Boolean> {
        return try {
            val member = groupMemberDao.getGroupMember(groupId, userId)
            if (member == null || !member.isActive) {
                return Result.success(false)
            }

            val userRole = member.getUserRole()
            val canPerform = when (action) {
                "MANAGE_MEMBERS" -> userRole.canManageMembers()
                "DELETE_MESSAGES" -> userRole.canDeleteMessages()
                "EDIT_GROUP_INFO" -> userRole.canEditGroupInfo()
                "PROMOTE_MEMBERS" -> userRole.canPromoteMembers()
                "REMOVE_MEMBERS" -> userRole.canRemoveMembers()
                else -> false
            }

            Result.success(canPerform)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncGroups(): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            // Get user's groups from Firebase
            val userGroupsSnapshot = userGroupsRef.child(currentUser.uid).get().await()
            val groupIds = mutableListOf<String>()
            
            userGroupsSnapshot.children.forEach { snapshot ->
                val groupId = snapshot.key
                if (groupId != null && snapshot.getValue(Boolean::class.java) == true) {
                    groupIds.add(groupId)
                }
            }

            // Fetch group details
            val groups = mutableListOf<Group>()
            groupIds.forEach { groupId ->
                try {
                    val groupSnapshot = groupsRef.child(groupId).get().await()
                    val group = groupSnapshot.getValue(Group::class.java)
                    if (group != null) {
                        groups.add(group)
                    }
                } catch (e: Exception) {
                    // Continue with other groups if one fails
                }
            }

            // Save to local database
            if (groups.isNotEmpty()) {
                groupDao.insertGroups(groups)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncGroupMembers(groupId: String): Result<Unit> {
        return try {
            val snapshot = groupMembersRef.child(groupId).get().await()
            val members = mutableListOf<GroupMember>()
            
            snapshot.children.forEach { memberSnapshot ->
                val member = memberSnapshot.getValue(GroupMember::class.java)
                if (member != null) {
                    members.add(member)
                }
            }
            
            if (members.isNotEmpty()) {
                groupMemberDao.insertGroupMembers(members)
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Extension function to get all groups (for internal use)
    private suspend fun GroupDao.getAllGroups(): List<Group> {
        return try {
            // This would need to be implemented in the DAO
            // For now, we'll use the flow and take the first emission
            getAllGroupsFlow().first()
        } catch (e: Exception) {
            emptyList()
        }
    }
}