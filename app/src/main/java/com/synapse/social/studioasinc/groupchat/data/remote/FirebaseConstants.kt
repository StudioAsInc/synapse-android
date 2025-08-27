package com.synapse.social.studioasinc.groupchat.data.remote

/**
 * Firebase Realtime Database structure and constants
 */
object FirebaseConstants {
    
    // Root paths
    const val GROUPS = "groups"
    const val GROUP_MEMBERS = "group_members"
    const val GROUP_MESSAGES = "group_messages"
    const val USERS = "users"
    const val USER_GROUPS = "user_groups" // userId -> list of groupIds
    const val GROUP_METADATA = "group_metadata"
    
    // Storage paths
    const val GROUP_ICONS = "group_icons"
    const val MESSAGE_ATTACHMENTS = "message_attachments"
    const val USER_PROFILES = "user_profiles"
    
    // FCM topics
    const val GROUP_TOPIC_PREFIX = "group_"
    
    // Indexing paths for better query performance
    const val GROUPS_BY_MEMBER = "groups_by_member" // userId/groupId -> true
    const val MESSAGES_BY_GROUP = "messages_by_group" // groupId/messageId -> timestamp
    const val MEMBERS_BY_GROUP = "members_by_group" // groupId/userId -> role
    const val USER_ONLINE_STATUS = "user_online_status" // userId -> { isOnline, lastSeen }
    
    // Message pagination
    const val MESSAGES_PAGE_SIZE = 20
    const val MAX_MESSAGE_LENGTH = 4000
    const val MAX_GROUP_NAME_LENGTH = 100
    const val MAX_GROUP_DESCRIPTION_LENGTH = 500
    
    // File upload limits
    const val MAX_FILE_SIZE_MB = 50
    const val MAX_IMAGE_SIZE_MB = 10
    const val MAX_VIDEO_SIZE_MB = 100
    const val MAX_AUDIO_SIZE_MB = 25
    
    // Group limits
    const val MAX_GROUP_MEMBERS = 256
    const val MAX_GROUPS_PER_USER = 100
    
    /**
     * Firebase Realtime Database structure:
     * 
     * groups/
     *   {groupId}/
     *     id: String
     *     name: String
     *     description: String
     *     iconUrl: String
     *     createdBy: String
     *     createdAt: ServerValue.TIMESTAMP
     *     updatedAt: ServerValue.TIMESTAMP
     *     memberCount: Number
     *     isPrivate: Boolean
     *     maxMembers: Number
     *     lastMessageId: String
     *     lastMessageText: String
     *     lastMessageTime: Number
     *     lastMessageSender: String
     *     isActive: Boolean
     *     settings: Object
     * 
     * group_members/
     *   {groupId}/
     *     {userId}/
     *       id: String
     *       groupId: String
     *       userId: String
     *       userName: String
     *       userPhotoUrl: String
     *       role: String (OWNER, ADMIN, MANAGER, MEMBER)
     *       joinedAt: ServerValue.TIMESTAMP
     *       addedBy: String
     *       isActive: Boolean
     *       lastSeenAt: Number
     * 
     * group_messages/
     *   {groupId}/
     *     {messageId}/
     *       id: String
     *       groupId: String
     *       senderId: String
     *       senderName: String
     *       senderPhotoUrl: String
     *       text: String
     *       timestamp: ServerValue.TIMESTAMP
     *       messageType: String
     *       attachments: Array
     *       replyToMessageId: String
     *       isEdited: Boolean
     *       editedAt: Number
     *       isDeleted: Boolean
     *       deletedAt: Number
     *       seenBy: Object (userId -> timestamp)
     *       reactions: Object (emoji -> [userIds])
     *       isSystemMessage: Boolean
     *       systemMessageType: String
     *       deliveryStatus: String
     *       priority: String
     * 
     * users/
     *   {userId}/
     *     id: String
     *     username: String
     *     displayName: String
     *     email: String
     *     photoUrl: String
     *     bio: String
     *     isOnline: Boolean
     *     lastSeen: ServerValue.TIMESTAMP
     *     fcmToken: String
     *     isActive: Boolean
     *     createdAt: ServerValue.TIMESTAMP
     *     updatedAt: ServerValue.TIMESTAMP
     *     phoneNumber: String
     *     isVerified: Boolean
     * 
     * user_groups/
     *   {userId}/
     *     {groupId}: Boolean
     * 
     * groups_by_member/
     *   {userId}/
     *     {groupId}: Boolean
     * 
     * members_by_group/
     *   {groupId}/
     *     {userId}: String (role)
     * 
     * user_online_status/
     *   {userId}/
     *     isOnline: Boolean
     *     lastSeen: Number
     */
}