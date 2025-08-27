package com.synapse.social.studioasinc.groupchat.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ServerValue
import kotlinx.parcelize.Parcelize

/**
 * Represents a group chat
 */
@Parcelize
@Entity(tableName = "groups")
@IgnoreExtraProperties
data class Group(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val iconUrl: String = "",
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val memberCount: Int = 0,
    val isPrivate: Boolean = false,
    val maxMembers: Int = 256,
    val lastMessageId: String = "",
    val lastMessageText: String = "",
    val lastMessageTime: Long = 0,
    val lastMessageSender: String = "",
    val isActive: Boolean = true,
    val settings: GroupSettings = GroupSettings()
) : Parcelable {
    
    constructor() : this("")
    
    fun getServerTimestamp(): Map<String, String> = ServerValue.TIMESTAMP
    
    fun canAddMoreMembers(): Boolean = memberCount < maxMembers
    
    fun getDisplayName(): String = name.ifEmpty { "Unnamed Group" }
}

@Parcelize
@IgnoreExtraProperties
data class GroupSettings(
    val allowMembersToAddOthers: Boolean = false,
    val allowMembersToEditInfo: Boolean = false,
    val onlyAdminsCanMessage: Boolean = false,
    val disappearingMessagesEnabled: Boolean = false,
    val disappearingMessagesDuration: Long = 0, // in milliseconds
    val muteNotifications: Boolean = false,
    val allowMediaSharing: Boolean = true,
    val allowFileSharing: Boolean = true,
    val maxFileSize: Long = 50 * 1024 * 1024 // 50MB
) : Parcelable {
    constructor() : this(false)
}