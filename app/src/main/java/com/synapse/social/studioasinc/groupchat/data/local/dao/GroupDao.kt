package com.synapse.social.studioasinc.groupchat.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.synapse.social.studioasinc.groupchat.data.model.Group
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    
    @Query("SELECT * FROM groups WHERE isActive = 1 ORDER BY lastMessageTime DESC")
    fun getAllGroupsPaged(): PagingSource<Int, Group>
    
    @Query("SELECT * FROM groups WHERE isActive = 1 ORDER BY lastMessageTime DESC")
    fun getAllGroupsFlow(): Flow<List<Group>>
    
    @Query("SELECT * FROM groups WHERE id = :groupId")
    suspend fun getGroupById(groupId: String): Group?
    
    @Query("SELECT * FROM groups WHERE id = :groupId")
    fun getGroupByIdFlow(groupId: String): Flow<Group?>
    
    @Query("SELECT * FROM groups WHERE name LIKE '%' || :query || '%' AND isActive = 1")
    suspend fun searchGroups(query: String): List<Group>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: Group)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<Group>)
    
    @Update
    suspend fun updateGroup(group: Group)
    
    @Query("UPDATE groups SET lastMessageId = :messageId, lastMessageText = :messageText, lastMessageTime = :messageTime, lastMessageSender = :sender WHERE id = :groupId")
    suspend fun updateLastMessage(groupId: String, messageId: String, messageText: String, messageTime: Long, sender: String)
    
    @Query("UPDATE groups SET memberCount = :count WHERE id = :groupId")
    suspend fun updateMemberCount(groupId: String, count: Int)
    
    @Query("UPDATE groups SET isActive = 0 WHERE id = :groupId")
    suspend fun markGroupAsInactive(groupId: String)
    
    @Query("DELETE FROM groups WHERE id = :groupId")
    suspend fun deleteGroup(groupId: String)
    
    @Query("DELETE FROM groups")
    suspend fun deleteAllGroups()
    
    @Query("SELECT COUNT(*) FROM groups WHERE isActive = 1")
    suspend fun getActiveGroupCount(): Int
}