package com.synapse.social.studioasinc.groupchat.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

/**
 * Represents a member of a group
 */
@Parcelize
@Entity(tableName = "group_members")
@IgnoreExtraProperties
data class GroupMember(
    @PrimaryKey
    val id: String = "",
    val groupId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String = "",
    val role: String = UserRole.MEMBER.name,
    val joinedAt: Long = System.currentTimeMillis(),
    val addedBy: String = "",
    val isActive: Boolean = true,
    val lastSeenAt: Long = System.currentTimeMillis()
) : Parcelable {
    
    constructor() : this("")
    
    fun getUserRole(): UserRole = UserRole.fromString(role)
    
    fun canBePromotedBy(promotoerRole: UserRole): Boolean {
        val currentRole = getUserRole()
        return when (promotoerRole) {
            UserRole.OWNER -> currentRole != UserRole.OWNER
            UserRole.ADMIN -> currentRole in listOf(UserRole.MEMBER, UserRole.MANAGER)
            else -> false
        }
    }
    
    fun canBeDemotedBy(demoterRole: UserRole): Boolean {
        val currentRole = getUserRole()
        return when (demoterRole) {
            UserRole.OWNER -> currentRole != UserRole.OWNER
            UserRole.ADMIN -> currentRole == UserRole.MANAGER
            else -> false
        }
    }
}