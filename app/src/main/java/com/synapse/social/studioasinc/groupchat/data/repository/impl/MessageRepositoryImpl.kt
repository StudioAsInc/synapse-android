package com.synapse.social.studioasinc.groupchat.data.repository.impl

import androidx.paging.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMessageDao
import com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao
import com.synapse.social.studioasinc.groupchat.data.model.*
import com.synapse.social.studioasinc.groupchat.data.remote.FirebaseConstants
import com.synapse.social.studioasinc.groupchat.data.repository.MessageRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor(
    private val messageDao: GroupMessageDao,
    private val memberDao: GroupMemberDao,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage
) : MessageRepository {

    private val messagesRef = firebaseDatabase.getReference(FirebaseConstants.GROUP_MESSAGES)
    private val groupsRef = firebaseDatabase.getReference(FirebaseConstants.GROUPS)
    private val storageRef = firebaseStorage.reference
    
    private val messageListeners = mutableMapOf<String, ChildEventListener>()

    override fun getGroupMessagesPaged(groupId: String): Flow<PagingData<GroupMessage>> {
        return Pager(
            config = PagingConfig(
                pageSize = FirebaseConstants.MESSAGES_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { messageDao.getGroupMessagesPaged(groupId) }
        ).flow
    }

    override fun getRecentMessagesFlow(groupId: String, limit: Int): Flow<List<GroupMessage>> {
        return messageDao.getRecentMessagesFlow(groupId, limit)
    }

    override suspend fun getRecentMessages(groupId: String, limit: Int): Result<List<GroupMessage>> {
        return try {
            val messages = messageDao.getRecentMessages(groupId, limit)
            Result.success(messages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessageById(messageId: String): Result<GroupMessage?> {
        return try {
            val message = messageDao.getMessageById(messageId)
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMessageByIdFlow(messageId: String): Flow<GroupMessage?> {
        return messageDao.getMessageByIdFlow(messageId)
    }

    override suspend fun sendMessage(message: GroupMessage): Result<String> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            // Verify user is a member of the group
            val member = memberDao.getGroupMember(message.groupId, currentUser.uid)
            if (member == null || !member.isActive) {
                return Result.failure(Exception("User is not a member of this group"))
            }

            val messageId = messagesRef.child(message.groupId).push().key
                ?: return Result.failure(Exception("Failed to generate message ID"))

            val finalMessage = message.copy(
                id = messageId,
                senderId = currentUser.uid,
                senderName = currentUser.displayName ?: "",
                senderPhotoUrl = currentUser.photoUrl?.toString() ?: "",
                timestamp = System.currentTimeMillis(),
                deliveryStatus = DeliveryStatus.SENDING.name
            )

            // Save to local database first for immediate UI update
            messageDao.insertMessage(finalMessage)

            // Update group's last message info
            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.GROUP_MESSAGES}/${message.groupId}/$messageId" to finalMessage,
                "/${FirebaseConstants.GROUPS}/${message.groupId}/lastMessageId" to messageId,
                "/${FirebaseConstants.GROUPS}/${message.groupId}/lastMessageText" to message.text,
                "/${FirebaseConstants.GROUPS}/${message.groupId}/lastMessageTime" to ServerValue.TIMESTAMP,
                "/${FirebaseConstants.GROUPS}/${message.groupId}/lastMessageSender" to currentUser.uid
            )

            firebaseDatabase.reference.updateChildren(updates).await()

            // Update delivery status
            messageDao.updateDeliveryStatus(messageId, DeliveryStatus.SENT.name)

            Result.success(messageId)
        } catch (e: Exception) {
            // Update message status to failed
            try {
                messageDao.updateDeliveryStatus(message.id, DeliveryStatus.FAILED.name)
            } catch (updateError: Exception) {
                // Ignore update error
            }
            Result.failure(e)
        }
    }

    override suspend fun sendMessageWithAttachments(
        message: GroupMessage, 
        attachmentFiles: List<File>
    ): Result<String> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            // Upload attachments first
            val attachments = mutableListOf<MessageAttachment>()
            attachmentFiles.forEach { file ->
                val uploadResult = uploadAttachment(message.groupId, file, getAttachmentType(file))
                if (uploadResult.isSuccess) {
                    attachments.add(uploadResult.getOrThrow())
                } else {
                    return Result.failure(uploadResult.exceptionOrNull() ?: Exception("Failed to upload attachment"))
                }
            }

            val messageWithAttachments = message.copy(
                attachments = attachments,
                messageType = if (attachments.isNotEmpty()) attachments.first().type else MessageType.TEXT.name
            )

            return sendMessage(messageWithAttachments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun editMessage(messageId: String, newText: String): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val message = messageDao.getMessageById(messageId)
                ?: return Result.failure(Exception("Message not found"))

            if (!message.canBeEditedBy(currentUser.uid)) {
                return Result.failure(Exception("Cannot edit this message"))
            }

            val editedAt = System.currentTimeMillis()
            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.GROUP_MESSAGES}/${message.groupId}/$messageId/text" to newText,
                "/${FirebaseConstants.GROUP_MESSAGES}/${message.groupId}/$messageId/isEdited" to true,
                "/${FirebaseConstants.GROUP_MESSAGES}/${message.groupId}/$messageId/editedAt" to editedAt
            )

            firebaseDatabase.reference.updateChildren(updates).await()
            messageDao.editMessage(messageId, newText, editedAt)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMessage(messageId: String, isHardDelete: Boolean): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.failure(Exception("User not authenticated"))

            val message = messageDao.getMessageById(messageId)
                ?: return Result.failure(Exception("Message not found"))

            // Check if user can delete the message
            val member = memberDao.getGroupMember(message.groupId, currentUser.uid)
            if (member == null || !message.canBeDeletedBy(currentUser.uid, member.getUserRole())) {
                return Result.failure(Exception("Cannot delete this message"))
            }

            if (isHardDelete) {
                // Permanently delete from Firebase and local
                messagesRef.child(message.groupId).child(messageId).removeValue().await()
                messageDao.permanentlyDeleteMessage(messageId)
            } else {
                // Soft delete - mark as deleted
                val deletedAt = System.currentTimeMillis()
                val updates = hashMapOf<String, Any>(
                    "/${FirebaseConstants.GROUP_MESSAGES}/${message.groupId}/$messageId/isDeleted" to true,
                    "/${FirebaseConstants.GROUP_MESSAGES}/${message.groupId}/$messageId/deletedAt" to deletedAt
                )

                firebaseDatabase.reference.updateChildren(updates).await()
                messageDao.deleteMessage(messageId, deletedAt)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchMessages(groupId: String, query: String): Result<List<GroupMessage>> {
        return try {
            val messages = messageDao.searchMessages(groupId, query)
            Result.success(messages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markMessageAsSeen(messageId: String, userId: String): Result<Unit> {
        return try {
            val message = messageDao.getMessageById(messageId)
                ?: return Result.failure(Exception("Message not found"))

            val currentSeenBy = message.seenBy.toMutableMap()
            currentSeenBy[userId] = System.currentTimeMillis()

            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.GROUP_MESSAGES}/${message.groupId}/$messageId/seenBy" to currentSeenBy
            )

            firebaseDatabase.reference.updateChildren(updates).await()
            messageDao.updateSeenBy(messageId, currentSeenBy)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markMessagesAsSeen(groupId: String, userId: String): Result<Unit> {
        return try {
            val messages = messageDao.getRecentMessages(groupId, 50)
            val timestamp = System.currentTimeMillis()
            
            val updates = hashMapOf<String, Any>()
            messages.forEach { message ->
                if (!message.isSeenBy(userId) && message.senderId != userId) {
                    val currentSeenBy = message.seenBy.toMutableMap()
                    currentSeenBy[userId] = timestamp
                    updates["/${FirebaseConstants.GROUP_MESSAGES}/$groupId/${message.id}/seenBy"] = currentSeenBy
                }
            }

            if (updates.isNotEmpty()) {
                firebaseDatabase.reference.updateChildren(updates).await()
                
                // Update local database
                messages.forEach { message ->
                    if (!message.isSeenBy(userId) && message.senderId != userId) {
                        val currentSeenBy = message.seenBy.toMutableMap()
                        currentSeenBy[userId] = timestamp
                        messageDao.updateSeenBy(message.id, currentSeenBy)
                    }
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateDeliveryStatus(messageId: String, status: String): Result<Unit> {
        return try {
            val message = messageDao.getMessageById(messageId)
                ?: return Result.failure(Exception("Message not found"))

            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.GROUP_MESSAGES}/${message.groupId}/$messageId/deliveryStatus" to status
            )

            firebaseDatabase.reference.updateChildren(updates).await()
            messageDao.updateDeliveryStatus(messageId, status)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUnreadMessageCount(groupId: String, userId: String): Result<Int> {
        return try {
            val count = messageDao.getUnreadMessageCount(groupId, userId)
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addReaction(messageId: String, userId: String, emoji: String): Result<Unit> {
        return try {
            val message = messageDao.getMessageById(messageId)
                ?: return Result.failure(Exception("Message not found"))

            val currentReactions = message.reactions.toMutableMap()
            val emojiUsers = currentReactions[emoji]?.toMutableList() ?: mutableListOf()
            
            if (!emojiUsers.contains(userId)) {
                emojiUsers.add(userId)
                currentReactions[emoji] = emojiUsers
            }

            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.GROUP_MESSAGES}/${message.groupId}/$messageId/reactions" to currentReactions
            )

            firebaseDatabase.reference.updateChildren(updates).await()
            messageDao.updateReactions(messageId, currentReactions)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeReaction(messageId: String, userId: String, emoji: String): Result<Unit> {
        return try {
            val message = messageDao.getMessageById(messageId)
                ?: return Result.failure(Exception("Message not found"))

            val currentReactions = message.reactions.toMutableMap()
            val emojiUsers = currentReactions[emoji]?.toMutableList() ?: mutableListOf()
            
            emojiUsers.remove(userId)
            if (emojiUsers.isEmpty()) {
                currentReactions.remove(emoji)
            } else {
                currentReactions[emoji] = emojiUsers
            }

            val updates = hashMapOf<String, Any>(
                "/${FirebaseConstants.GROUP_MESSAGES}/${message.groupId}/$messageId/reactions" to currentReactions
            )

            firebaseDatabase.reference.updateChildren(updates).await()
            messageDao.updateReactions(messageId, currentReactions)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessageReactions(messageId: String): Result<Map<String, List<String>>> {
        return try {
            val message = messageDao.getMessageById(messageId)
                ?: return Result.failure(Exception("Message not found"))

            Result.success(message.reactions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadAttachment(groupId: String, file: File, attachmentType: String): Result<MessageAttachment> {
        return try {
            val fileName = "${UUID.randomUUID()}_${file.name}"
            val fileRef = storageRef
                .child(FirebaseConstants.MESSAGE_ATTACHMENTS)
                .child(groupId)
                .child(fileName)

            val uploadTask = fileRef.putFile(android.net.Uri.fromFile(file)).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()

            val attachment = MessageAttachment(
                id = UUID.randomUUID().toString(),
                type = attachmentType,
                url = downloadUrl.toString(),
                fileName = file.name,
                fileSize = file.length(),
                mimeType = getMimeType(file),
                isUploading = false,
                uploadProgress = 100f
            )

            Result.success(attachment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun downloadAttachment(attachment: MessageAttachment, destinationFile: File): Result<Unit> {
        return try {
            val fileRef = firebaseStorage.getReferenceFromUrl(attachment.url)
            fileRef.getFile(destinationFile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAttachment(attachment: MessageAttachment): Result<Unit> {
        return try {
            val fileRef = firebaseStorage.getReferenceFromUrl(attachment.url)
            fileRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun replyToMessage(originalMessageId: String, replyMessage: GroupMessage): Result<String> {
        return try {
            val messageWithReply = replyMessage.copy(replyToMessageId = originalMessageId)
            sendMessage(messageWithReply)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessageReplies(messageId: String): Result<List<GroupMessage>> {
        return try {
            val message = messageDao.getMessageById(messageId)
                ?: return Result.failure(Exception("Message not found"))

            // This would require a query to find messages with replyToMessageId = messageId
            // For now, returning empty list
            Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPendingMessages(groupId: String): Result<List<GroupMessage>> {
        return try {
            val messages = messageDao.getPendingMessages(groupId)
            Result.success(messages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun retryFailedMessages(groupId: String): Result<Unit> {
        return try {
            val failedMessages = messageDao.getPendingMessages(groupId)
            failedMessages.forEach { message ->
                try {
                    sendMessage(message)
                } catch (e: Exception) {
                    // Continue with other messages
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncMessages(groupId: String): Result<Unit> {
        return try {
            // Get recent messages from Firebase
            val snapshot = messagesRef.child(groupId)
                .orderByChild("timestamp")
                .limitToLast(100)
                .get()
                .await()

            val messages = mutableListOf<GroupMessage>()
            snapshot.children.forEach { messageSnapshot ->
                val message = messageSnapshot.getValue(GroupMessage::class.java)
                if (message != null) {
                    messages.add(message)
                }
            }

            if (messages.isNotEmpty()) {
                messageDao.insertMessages(messages)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loadMoreMessages(groupId: String, beforeTimestamp: Long, limit: Int): Result<List<GroupMessage>> {
        return try {
            val snapshot = messagesRef.child(groupId)
                .orderByChild("timestamp")
                .endAt(beforeTimestamp.toDouble())
                .limitToLast(limit)
                .get()
                .await()

            val messages = mutableListOf<GroupMessage>()
            snapshot.children.forEach { messageSnapshot ->
                val message = messageSnapshot.getValue(GroupMessage::class.java)
                if (message != null) {
                    messages.add(message)
                }
            }

            // Save to local database
            if (messages.isNotEmpty()) {
                messageDao.insertMessages(messages)
            }

            Result.success(messages.reversed()) // Return in chronological order
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessagesByUser(groupId: String, userId: String): Result<List<GroupMessage>> {
        return try {
            val messages = messageDao.getMessagesByUser(groupId, userId)
            Result.success(messages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessageCount(groupId: String): Result<Int> {
        return try {
            val count = messageDao.getMessageCount(groupId)
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun startListeningToMessages(groupId: String): Flow<GroupMessage> {
        return callbackFlow {
            val listener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(GroupMessage::class.java)
                    if (message != null) {
                        trySend(message)
                        // Save to local database
                        try {
                            kotlinx.coroutines.runBlocking {
                                messageDao.insertMessage(message)
                            }
                        } catch (e: Exception) {
                            // Ignore local save errors
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(GroupMessage::class.java)
                    if (message != null) {
                        trySend(message)
                        try {
                            kotlinx.coroutines.runBlocking {
                                messageDao.updateMessage(message)
                            }
                        } catch (e: Exception) {
                            // Ignore local save errors
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // Handle message deletion
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // Not needed for messages
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }

            messagesRef.child(groupId)
                .orderByChild("timestamp")
                .limitToLast(50)
                .addChildEventListener(listener)

            messageListeners[groupId] = listener

            awaitClose {
                messagesRef.child(groupId).removeEventListener(listener)
                messageListeners.remove(groupId)
            }
        }
    }

    override fun stopListeningToMessages(groupId: String) {
        messageListeners[groupId]?.let { listener ->
            messagesRef.child(groupId).removeEventListener(listener)
            messageListeners.remove(groupId)
        }
    }

    override fun startListeningToMessageUpdates(messageId: String): Flow<GroupMessage> {
        return callbackFlow {
            // This would require knowing which group the message belongs to
            // For now, returning empty flow
            awaitClose { }
        }
    }

    override fun stopListeningToMessageUpdates(messageId: String) {
        // Implementation for stopping specific message updates
    }

    private fun getAttachmentType(file: File): String {
        val mimeType = getMimeType(file)
        return when {
            mimeType.startsWith("image/") -> AttachmentType.IMAGE.name
            mimeType.startsWith("video/") -> AttachmentType.VIDEO.name
            mimeType.startsWith("audio/") -> AttachmentType.AUDIO.name
            else -> AttachmentType.FILE.name
        }
    }

    private fun getMimeType(file: File): String {
        return when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "mp4" -> "video/mp4"
            "mp3" -> "audio/mpeg"
            "pdf" -> "application/pdf"
            "doc", "docx" -> "application/msword"
            else -> "application/octet-stream"
        }
    }
}