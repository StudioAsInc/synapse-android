package com.synapse.social.studioasinc.groupchat.data.repository

import androidx.paging.PagingData
import com.synapse.social.studioasinc.groupchat.data.model.GroupMessage
import com.synapse.social.studioasinc.groupchat.data.model.MessageAttachment
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MessageRepository {
    
    // Message operations
    fun getGroupMessagesPaged(groupId: String): Flow<PagingData<GroupMessage>>
    fun getRecentMessagesFlow(groupId: String, limit: Int = 50): Flow<List<GroupMessage>>
    suspend fun getRecentMessages(groupId: String, limit: Int = 50): Result<List<GroupMessage>>
    suspend fun getMessageById(messageId: String): Result<GroupMessage?>
    fun getMessageByIdFlow(messageId: String): Flow<GroupMessage?>
    suspend fun sendMessage(message: GroupMessage): Result<String>
    suspend fun sendMessageWithAttachments(message: GroupMessage, attachmentFiles: List<File>): Result<String>
    suspend fun editMessage(messageId: String, newText: String): Result<Unit>
    suspend fun deleteMessage(messageId: String, isHardDelete: Boolean = false): Result<Unit>
    suspend fun searchMessages(groupId: String, query: String): Result<List<GroupMessage>>
    
    // Message status operations
    suspend fun markMessageAsSeen(messageId: String, userId: String): Result<Unit>
    suspend fun markMessagesAsSeen(groupId: String, userId: String): Result<Unit>
    suspend fun updateDeliveryStatus(messageId: String, status: String): Result<Unit>
    suspend fun getUnreadMessageCount(groupId: String, userId: String): Result<Int>
    
    // Reaction operations
    suspend fun addReaction(messageId: String, userId: String, emoji: String): Result<Unit>
    suspend fun removeReaction(messageId: String, userId: String, emoji: String): Result<Unit>
    suspend fun getMessageReactions(messageId: String): Result<Map<String, List<String>>>
    
    // Attachment operations
    suspend fun uploadAttachment(groupId: String, file: File, attachmentType: String): Result<MessageAttachment>
    suspend fun downloadAttachment(attachment: MessageAttachment, destinationFile: File): Result<Unit>
    suspend fun deleteAttachment(attachment: MessageAttachment): Result<Unit>
    
    // Reply operations
    suspend fun replyToMessage(originalMessageId: String, replyMessage: GroupMessage): Result<String>
    suspend fun getMessageReplies(messageId: String): Result<List<GroupMessage>>
    
    // Offline operations
    suspend fun getPendingMessages(groupId: String): Result<List<GroupMessage>>
    suspend fun retryFailedMessages(groupId: String): Result<Unit>
    suspend fun syncMessages(groupId: String): Result<Unit>
    
    // Message history and pagination
    suspend fun loadMoreMessages(groupId: String, beforeTimestamp: Long, limit: Int = 20): Result<List<GroupMessage>>
    suspend fun getMessagesByUser(groupId: String, userId: String): Result<List<GroupMessage>>
    suspend fun getMessageCount(groupId: String): Result<Int>
    
    // Real-time listeners
    fun startListeningToMessages(groupId: String): Flow<GroupMessage>
    fun stopListeningToMessages(groupId: String)
    fun startListeningToMessageUpdates(messageId: String): Flow<GroupMessage>
    fun stopListeningToMessageUpdates(messageId: String)
}