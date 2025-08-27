package com.synapse.social.studioasinc.groupchat.data.local.dao

import androidx.room.*
import com.synapse.social.studioasinc.groupchat.data.model.GroupMember
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupMemberDao {
    
    @Query("SELECT * FROM group_members WHERE groupId = :groupId AND isActive = 1 ORDER BY role DESC, joinedAt ASC")
    fun getGroupMembersFlow(groupId: String): Flow<List<GroupMember>>
    
    @Query("SELECT * FROM group_members WHERE groupId = :groupId AND isActive = 1 ORDER BY role DESC, joinedAt ASC")
    suspend fun getGroupMembers(groupId: String): List<GroupMember>
    
    @Query("SELECT * FROM group_members WHERE groupId = :groupId AND userId = :userId")
    suspend fun getGroupMember(groupId: String, userId: String): GroupMember?
    
    @Query("SELECT * FROM group_members WHERE groupId = :groupId AND userId = :userId")
    fun getGroupMemberFlow(groupId: String, userId: String): Flow<GroupMember?>
    
    @Query("SELECT * FROM group_members WHERE groupId = :groupId AND role IN ('OWNER', 'ADMIN') AND isActive = 1")
    suspend fun getGroupAdmins(groupId: String): List<GroupMember>
    
    @Query("SELECT * FROM group_members WHERE groupId = :groupId AND userName LIKE '%' || :query || '%' AND isActive = 1")
    suspend fun searchGroupMembers(groupId: String, query: String): List<GroupMember>
    
    @Query("SELECT COUNT(*) FROM group_members WHERE groupId = :groupId AND isActive = 1")
    suspend fun getGroupMemberCount(groupId: String): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupMember(member: GroupMember)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupMembers(members: List<GroupMember>)
    
    @Update
    suspend fun updateGroupMember(member: GroupMember)
    
    @Query("UPDATE group_members SET role = :role WHERE groupId = :groupId AND userId = :userId")
    suspend fun updateMemberRole(groupId: String, userId: String, role: String)
    
    @Query("UPDATE group_members SET isActive = 0 WHERE groupId = :groupId AND userId = :userId")
    suspend fun removeMember(groupId: String, userId: String)
    
    @Query("UPDATE group_members SET lastSeenAt = :timestamp WHERE groupId = :groupId AND userId = :userId")
    suspend fun updateLastSeen(groupId: String, userId: String, timestamp: Long)
    
    @Query("DELETE FROM group_members WHERE groupId = :groupId AND userId = :userId")
    suspend fun deleteGroupMember(groupId: String, userId: String)
    
    @Query("DELETE FROM group_members WHERE groupId = :groupId")
    suspend fun deleteAllGroupMembers(groupId: String)
    
    @Query("DELETE FROM group_members")
    suspend fun deleteAllMembers()
}