package com.synapse.social.studioasinc.groupchat.data.remote;

/**
 * Firebase Realtime Database structure and constants
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0010\b\n\u0002\b\n\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0014X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0014X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0014X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0014X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0014X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0014X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0014X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0014X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/remote/FirebaseConstants;", "", "<init>", "()V", "GROUPS", "", "GROUP_MEMBERS", "GROUP_MESSAGES", "USERS", "USER_GROUPS", "GROUP_METADATA", "GROUP_ICONS", "MESSAGE_ATTACHMENTS", "USER_PROFILES", "GROUP_TOPIC_PREFIX", "GROUPS_BY_MEMBER", "MESSAGES_BY_GROUP", "MEMBERS_BY_GROUP", "USER_ONLINE_STATUS", "MESSAGES_PAGE_SIZE", "", "MAX_MESSAGE_LENGTH", "MAX_GROUP_NAME_LENGTH", "MAX_GROUP_DESCRIPTION_LENGTH", "MAX_FILE_SIZE_MB", "MAX_IMAGE_SIZE_MB", "MAX_VIDEO_SIZE_MB", "MAX_AUDIO_SIZE_MB", "MAX_GROUP_MEMBERS", "MAX_GROUPS_PER_USER", "app_release"})
public final class FirebaseConstants {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GROUPS = "groups";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GROUP_MEMBERS = "group_members";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GROUP_MESSAGES = "group_messages";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USERS = "users";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_GROUPS = "user_groups";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GROUP_METADATA = "group_metadata";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GROUP_ICONS = "group_icons";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MESSAGE_ATTACHMENTS = "message_attachments";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_PROFILES = "user_profiles";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GROUP_TOPIC_PREFIX = "group_";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GROUPS_BY_MEMBER = "groups_by_member";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MESSAGES_BY_GROUP = "messages_by_group";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MEMBERS_BY_GROUP = "members_by_group";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String USER_ONLINE_STATUS = "user_online_status";
    public static final int MESSAGES_PAGE_SIZE = 20;
    public static final int MAX_MESSAGE_LENGTH = 4000;
    public static final int MAX_GROUP_NAME_LENGTH = 100;
    public static final int MAX_GROUP_DESCRIPTION_LENGTH = 500;
    public static final int MAX_FILE_SIZE_MB = 50;
    public static final int MAX_IMAGE_SIZE_MB = 10;
    public static final int MAX_VIDEO_SIZE_MB = 100;
    public static final int MAX_AUDIO_SIZE_MB = 25;
    public static final int MAX_GROUP_MEMBERS = 256;
    public static final int MAX_GROUPS_PER_USER = 100;
    @org.jetbrains.annotations.NotNull()
    public static final com.synapse.social.studioasinc.groupchat.data.remote.FirebaseConstants INSTANCE = null;
    
    private FirebaseConstants() {
        super();
    }
}