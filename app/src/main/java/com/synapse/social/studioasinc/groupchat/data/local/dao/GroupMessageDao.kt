package com.synapse.social.studioasinc.groupchat.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.synapse.social.studioasinc.groupchat.data.model.GroupMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupMessageDao {
    
    @Query("SELECT * FROM group_messages WHERE groupId = :groupId AND isDeleted = 0 ORDER BY timestamp DESC")
    fun getGroupMessagesPaged(groupId: String): PagingSource<Int, GroupMessage>
    
    @Query("SELECT * FROM group_messages WHERE groupId = :groupId AND isDeleted = 0 ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentMessages(groupId: String, limit: Int = 50): List<GroupMessage>
    
    @Query("SELECT * FROM group_messages WHERE groupId = :groupId AND isDeleted = 0 ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentMessagesFlow(groupId: String, limit: Int = 50): Flow<List<GroupMessage>>
    
    @Query("SELECT * FROM group_messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: String): GroupMessage?
    
    @Query("SELECT * FROM group_messages WHERE id = :messageId")
    fun getMessageByIdFlow(messageId: String): Flow<GroupMessage?>
    
    @Query("SELECT * FROM group_messages WHERE localId = :localId")
    suspend fun getMessageByLocalId(localId: String): GroupMessage?
    
    @Query("SELECT * FROM group_messages WHERE groupId = :groupId AND text LIKE '%' || :query || '%' AND isDeleted = 0 ORDER BY timestamp DESC")
    suspend fun searchMessages(groupId: String, query: String): List<GroupMessage>
    
    @Query("SELECT * FROM group_messages WHERE groupId = :groupId AND deliveryStatus = 'SENDING' OR deliveryStatus = 'FAILED'")
    suspend fun getPendingMessages(groupId: String): List<GroupMessage>
    
    @Query("SELECT * FROM group_messages WHERE groupId = :groupId AND senderId = :userId AND isDeleted = 0 ORDER BY timestamp DESC")
    suspend fun getMessagesByUser(groupId: String, userId: String): List<GroupMessage>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: GroupMessage)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<GroupMessage>)
    
    @Update
    suspend fun updateMessage(message: GroupMessage)
    
    @Query("UPDATE group_messages SET deliveryStatus = :status WHERE id = :messageId")
    suspend fun updateDeliveryStatus(messageId: String, status: String)
    
    @Query("UPDATE group_messages SET text = :newText, isEdited = 1, editedAt = :editedAt WHERE id = :messageId")
    suspend fun editMessage(messageId: String, newText: String, editedAt: Long)
    
    @Query("UPDATE group_messages SET isDeleted = 1, deletedAt = :deletedAt WHERE id = :messageId")
    suspend fun deleteMessage(messageId: String, deletedAt: Long)
    
    @Query("UPDATE group_messages SET seenBy = :seenBy WHERE id = :messageId")
    suspend fun updateSeenBy(messageId: String, seenBy: Map<String, Long>)
    
    @Query("UPDATE group_messages SET reactions = :reactions WHERE id = :messageId")
    suspend fun updateReactions(messageId: String, reactions: Map<String, List<String>>)
    
    @Query("DELETE FROM group_messages WHERE id = :messageId")
    suspend fun permanentlyDeleteMessage(messageId: String)
    
    @Query("DELETE FROM group_messages WHERE groupId = :groupId")
    suspend fun deleteAllGroupMessages(groupId: String)
    
    @Query("DELETE FROM group_messages")
    suspend fun deleteAllMessages()
    
    @Query("SELECT COUNT(*) FROM group_messages WHERE groupId = :groupId AND isDeleted = 0")
    suspend fun getMessageCount(groupId: String): Int
    
    @Query("SELECT COUNT(*) FROM group_messages WHERE groupId = :groupId AND senderId != :currentUserId AND NOT EXISTS (SELECT 1 FROM json_each(seenBy) WHERE json_each.key = :currentUserId)")
    suspend fun getUnreadMessageCount(groupId: String, currentUserId: String): Int
}