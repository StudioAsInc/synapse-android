package com.synapse.social.studioasinc.groupchat.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ServerValue
import kotlinx.parcelize.Parcelize

/**
 * Represents a message in a group chat
 */
@Parcelize
@Entity(tableName = "group_messages")
@IgnoreExtraProperties
data class GroupMessage(
    @PrimaryKey
    val id: String = "",
    val groupId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderPhotoUrl: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val messageType: String = MessageType.TEXT.name,
    val attachments: List<MessageAttachment> = emptyList(),
    val replyToMessageId: String = "",
    val isEdited: Boolean = false,
    val editedAt: Long = 0,
    val isDeleted: Boolean = false,
    val deletedAt: Long = 0,
    val seenBy: Map<String, Long> = emptyMap(), // userId to timestamp
    val reactions: Map<String, List<String>> = emptyMap(), // emoji to list of userIds
    val isSystemMessage: Boolean = false,
    val systemMessageType: String = "",
    val deliveryStatus: String = DeliveryStatus.SENDING.name,
    val localId: String = "", // For offline messages
    val priority: String = MessagePriority.NORMAL.name
) : Parcelable {
    
    constructor() : this("")
    
    fun getServerTimestamp(): Map<String, String> = ServerValue.TIMESTAMP
    
    fun toMessageType(): MessageType = MessageType.fromString(messageType)
    
    fun toDeliveryStatus(): DeliveryStatus = DeliveryStatus.fromString(deliveryStatus)
    
    fun toPriority(): MessagePriority = MessagePriority.fromString(priority)
    
    fun hasAttachments(): Boolean = attachments.isNotEmpty()
    
    fun isSeenBy(userId: String): Boolean = seenBy.containsKey(userId)
    
    fun getReactionCount(): Int = reactions.values.sumOf { it.size }
    
    fun hasUserReacted(userId: String, emoji: String): Boolean {
        return reactions[emoji]?.contains(userId) == true
    }
    
    fun canBeDeletedBy(userId: String, userRole: UserRole): Boolean {
        return senderId == userId || userRole.canDeleteMessages()
    }
    
    fun canBeEditedBy(userId: String): Boolean {
        return senderId == userId && !isDeleted && toMessageType() == MessageType.TEXT
    }
}

@Parcelize
@IgnoreExtraProperties
data class MessageAttachment(
    val id: String = "",
    val type: String = AttachmentType.IMAGE.name,
    val url: String = "",
    val fileName: String = "",
    val fileSize: Long = 0,
    val mimeType: String = "",
    val thumbnailUrl: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val duration: Long = 0, // for audio/video
    val isUploading: Boolean = false,
    val uploadProgress: Float = 0f,
    val localPath: String = ""
) : Parcelable {
    
    constructor() : this("")
    
    fun getAttachmentType(): AttachmentType = AttachmentType.fromString(type)
    
    fun isImage(): Boolean = getAttachmentType() == AttachmentType.IMAGE
    fun isVideo(): Boolean = getAttachmentType() == AttachmentType.VIDEO
    fun isAudio(): Boolean = getAttachmentType() == AttachmentType.AUDIO
    fun isFile(): Boolean = getAttachmentType() == AttachmentType.FILE
}

enum class MessageType(val displayName: String) {
    TEXT("Text"),
    IMAGE("Image"),
    VIDEO("Video"),
    AUDIO("Audio"),
    FILE("File"),
    LOCATION("Location"),
    SYSTEM("System");

    companion object {
        fun fromString(type: String): MessageType {
            return values().find { it.name.equals(type, ignoreCase = true) } ?: TEXT
        }
    }
}

enum class AttachmentType(val displayName: String) {
    IMAGE("Image"),
    VIDEO("Video"),
    AUDIO("Audio"),
    FILE("File");

    companion object {
        fun fromString(type: String): AttachmentType {
            return values().find { it.name.equals(type, ignoreCase = true) } ?: FILE
        }
    }
}

enum class DeliveryStatus(val displayName: String) {
    SENDING("Sending"),
    SENT("Sent"),
    DELIVERED("Delivered"),
    SEEN("Seen"),
    FAILED("Failed");

    companion object {
        fun fromString(status: String): DeliveryStatus {
            return values().find { it.name.equals(status, ignoreCase = true) } ?: SENDING
        }
    }
}

enum class MessagePriority(val displayName: String, val value: Int) {
    LOW("Low", 1),
    NORMAL("Normal", 2),
    HIGH("High", 3),
    URGENT("Urgent", 4);

    companion object {
        fun fromString(priority: String): MessagePriority {
            return values().find { it.name.equals(priority, ignoreCase = true) } ?: NORMAL
        }
    }
}