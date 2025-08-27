package com.synapse.social.studioasinc.groupchat.data.model

/**
 * Represents the role of a user within a group
 */
enum class UserRole(val displayName: String, val priority: Int) {
    OWNER("Owner", 3),
    ADMIN("Admin", 2),
    MANAGER("Manager", 1),
    MEMBER("Member", 0);

    companion object {
        fun fromString(role: String): UserRole {
            return values().find { it.name.equals(role, ignoreCase = true) } ?: MEMBER
        }
    }

    fun canManageMembers(): Boolean = this in listOf(OWNER, ADMIN, MANAGER)
    fun canDeleteMessages(): Boolean = this in listOf(OWNER, ADMIN)
    fun canEditGroupInfo(): Boolean = this in listOf(OWNER, ADMIN)
    fun canPromoteMembers(): Boolean = this == OWNER || (this == ADMIN && priority > 1)
    fun canRemoveMembers(): Boolean = this in listOf(OWNER, ADMIN, MANAGER)
}