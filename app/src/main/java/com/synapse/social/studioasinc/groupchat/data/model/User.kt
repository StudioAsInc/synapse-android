package com.synapse.social.studioasinc.groupchat.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

/**
 * Represents a user in the system
 */
@Parcelize
@Entity(tableName = "users")
@IgnoreExtraProperties
data class User(
    @PrimaryKey
    val id: String = "",
    val username: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val bio: String = "",
    val isOnline: Boolean = false,
    val lastSeen: Long = System.currentTimeMillis(),
    val fcmToken: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val phoneNumber: String = "",
    val isVerified: Boolean = false
) : Parcelable {
    
    constructor() : this("")
    
    fun getDisplayNameOrUsername(): String {
        return displayName.ifEmpty { username.ifEmpty { "Unknown User" } }
    }
    
    fun getInitials(): String {
        val name = getDisplayNameOrUsername()
        return if (name.contains(" ")) {
            name.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")
        } else {
            name.take(2).uppercase()
        }
    }
    
    fun isOnlineRecently(): Boolean {
        return isOnline || (System.currentTimeMillis() - lastSeen) < 5 * 60 * 1000 // 5 minutes
    }
}