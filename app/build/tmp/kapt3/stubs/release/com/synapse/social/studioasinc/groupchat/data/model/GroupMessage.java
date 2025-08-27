package com.synapse.social.studioasinc.groupchat.data.model;

/**
 * Represents a message in a group chat
 */
@com.google.firebase.database.IgnoreExtraProperties()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u00fd\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0012\u001a\u00020\n\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0014\u001a\u00020\n\u0012\u0014\b\u0002\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\n0\u0016\u0012\u001a\b\u0002\u0010\u0017\u001a\u0014\u0012\u0004\u0012\u00020\u0003\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\r0\u0016\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u0003\u00a2\u0006\u0004\b\u001d\u0010\u001eB\t\b\u0016\u00a2\u0006\u0004\b\u001d\u0010\u001fJ\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0016J\u0006\u0010)\u001a\u000208J\u0006\u00104\u001a\u000209J\u0006\u00106\u001a\u00020:J\u0006\u0010;\u001a\u00020\u0011J\u000e\u0010<\u001a\u00020\u00112\u0006\u0010=\u001a\u00020\u0003J\u0006\u0010>\u001a\u00020?J\u0016\u0010@\u001a\u00020\u00112\u0006\u0010=\u001a\u00020\u00032\u0006\u0010A\u001a\u00020\u0003J\u0016\u0010B\u001a\u00020\u00112\u0006\u0010=\u001a\u00020\u00032\u0006\u0010C\u001a\u00020DJ\u000e\u0010E\u001a\u00020\u00112\u0006\u0010=\u001a\u00020\u0003J\t\u0010F\u001a\u00020\u0003H\u00c6\u0003J\t\u0010G\u001a\u00020\u0003H\u00c6\u0003J\t\u0010H\u001a\u00020\u0003H\u00c6\u0003J\t\u0010I\u001a\u00020\u0003H\u00c6\u0003J\t\u0010J\u001a\u00020\u0003H\u00c6\u0003J\t\u0010K\u001a\u00020\u0003H\u00c6\u0003J\t\u0010L\u001a\u00020\nH\u00c6\u0003J\t\u0010M\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010N\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u00c6\u0003J\t\u0010O\u001a\u00020\u0003H\u00c6\u0003J\t\u0010P\u001a\u00020\u0011H\u00c6\u0003J\t\u0010Q\u001a\u00020\nH\u00c6\u0003J\t\u0010R\u001a\u00020\u0011H\u00c6\u0003J\t\u0010S\u001a\u00020\nH\u00c6\u0003J\u0015\u0010T\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\n0\u0016H\u00c6\u0003J\u001b\u0010U\u001a\u0014\u0012\u0004\u0012\u00020\u0003\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\r0\u0016H\u00c6\u0003J\t\u0010V\u001a\u00020\u0011H\u00c6\u0003J\t\u0010W\u001a\u00020\u0003H\u00c6\u0003J\t\u0010X\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0003H\u00c6\u0003J\u00ff\u0001\u0010[\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00032\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\n2\b\b\u0002\u0010\u0013\u001a\u00020\u00112\b\b\u0002\u0010\u0014\u001a\u00020\n2\u0014\b\u0002\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\n0\u00162\u001a\b\u0002\u0010\u0017\u001a\u0014\u0012\u0004\u0012\u00020\u0003\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\r0\u00162\b\b\u0002\u0010\u0018\u001a\u00020\u00112\b\b\u0002\u0010\u0019\u001a\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u00032\b\b\u0002\u0010\u001b\u001a\u00020\u00032\b\b\u0002\u0010\u001c\u001a\u00020\u0003H\u00c6\u0001J\u0006\u0010\\\u001a\u00020?J\u0013\u0010]\u001a\u00020\u00112\b\u0010^\u001a\u0004\u0018\u00010_H\u00d6\u0003J\t\u0010`\u001a\u00020?H\u00d6\u0001J\t\u0010a\u001a\u00020\u0003H\u00d6\u0001J\u0016\u0010b\u001a\u00020c2\u0006\u0010d\u001a\u00020e2\u0006\u0010f\u001a\u00020?R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010!R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010!R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010!R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010!R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010!R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010(R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010!R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u0011\u0010\u000f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010!R\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010-R\u0011\u0010\u0012\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010(R\u0011\u0010\u0013\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010-R\u0011\u0010\u0014\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010(R\u001d\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\n0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u00101R#\u0010\u0017\u001a\u0014\u0012\u0004\u0012\u00020\u0003\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\r0\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u00101R\u0011\u0010\u0018\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010-R\u0011\u0010\u0019\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010!R\u0011\u0010\u001a\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010!R\u0011\u0010\u001b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010!R\u0011\u0010\u001c\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010!\u00a8\u0006g"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;", "Landroid/os/Parcelable;", "id", "", "groupId", "senderId", "senderName", "senderPhotoUrl", "text", "timestamp", "", "messageType", "attachments", "", "Lcom/synapse/social/studioasinc/groupchat/data/model/MessageAttachment;", "replyToMessageId", "isEdited", "", "editedAt", "isDeleted", "deletedAt", "seenBy", "", "reactions", "isSystemMessage", "systemMessageType", "deliveryStatus", "localId", "priority", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/util/List;Ljava/lang/String;ZJZJLjava/util/Map;Ljava/util/Map;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "()V", "getId", "()Ljava/lang/String;", "getGroupId", "getSenderId", "getSenderName", "getSenderPhotoUrl", "getText", "getTimestamp", "()J", "getMessageType", "getAttachments", "()Ljava/util/List;", "getReplyToMessageId", "()Z", "getEditedAt", "getDeletedAt", "getSeenBy", "()Ljava/util/Map;", "getReactions", "getSystemMessageType", "getDeliveryStatus", "getLocalId", "getPriority", "getServerTimestamp", "Lcom/synapse/social/studioasinc/groupchat/data/model/MessageType;", "Lcom/synapse/social/studioasinc/groupchat/data/model/DeliveryStatus;", "Lcom/synapse/social/studioasinc/groupchat/data/model/MessagePriority;", "hasAttachments", "isSeenBy", "userId", "getReactionCount", "", "hasUserReacted", "emoji", "canBeDeletedBy", "userRole", "Lcom/synapse/social/studioasinc/groupchat/data/model/UserRole;", "canBeEditedBy", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component20", "component21", "copy", "describeContents", "equals", "other", "", "hashCode", "toString", "writeToParcel", "", "dest", "Landroid/os/Parcel;", "flags", "app_release"})
@kotlinx.parcelize.Parcelize()
@androidx.room.Entity(tableName = "group_messages")
public final class GroupMessage implements android.os.Parcelable {
    @androidx.room.PrimaryKey()
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String groupId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String senderId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String senderName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String senderPhotoUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String text = null;
    private final long timestamp = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String messageType = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.synapse.social.studioasinc.groupchat.data.model.MessageAttachment> attachments = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String replyToMessageId = null;
    private final boolean isEdited = false;
    private final long editedAt = 0L;
    private final boolean isDeleted = false;
    private final long deletedAt = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.lang.Long> seenBy = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.util.List<java.lang.String>> reactions = null;
    private final boolean isSystemMessage = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String systemMessageType = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String deliveryStatus = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String localId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String priority = null;
    
    /**
     * Represents a message in a group chat
     */
    @java.lang.Override()
    public final int describeContents() {
        return 0;
    }
    
    /**
     * Represents a message in a group chat
     */
    @java.lang.Override()
    public final void writeToParcel(@org.jetbrains.annotations.NotNull()
    android.os.Parcel dest, int flags) {
    }
    
    public GroupMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String senderId, @org.jetbrains.annotations.NotNull()
    java.lang.String senderName, @org.jetbrains.annotations.NotNull()
    java.lang.String senderPhotoUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String text, long timestamp, @org.jetbrains.annotations.NotNull()
    java.lang.String messageType, @org.jetbrains.annotations.NotNull()
    java.util.List<com.synapse.social.studioasinc.groupchat.data.model.MessageAttachment> attachments, @org.jetbrains.annotations.NotNull()
    java.lang.String replyToMessageId, boolean isEdited, long editedAt, boolean isDeleted, long deletedAt, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Long> seenBy, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, ? extends java.util.List<java.lang.String>> reactions, boolean isSystemMessage, @org.jetbrains.annotations.NotNull()
    java.lang.String systemMessageType, @org.jetbrains.annotations.NotNull()
    java.lang.String deliveryStatus, @org.jetbrains.annotations.NotNull()
    java.lang.String localId, @org.jetbrains.annotations.NotNull()
    java.lang.String priority) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getGroupId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSenderId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSenderName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSenderPhotoUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getText() {
        return null;
    }
    
    public final long getTimestamp() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMessageType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.synapse.social.studioasinc.groupchat.data.model.MessageAttachment> getAttachments() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getReplyToMessageId() {
        return null;
    }
    
    public final boolean isEdited() {
        return false;
    }
    
    public final long getEditedAt() {
        return 0L;
    }
    
    public final boolean isDeleted() {
        return false;
    }
    
    public final long getDeletedAt() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Long> getSeenBy() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.List<java.lang.String>> getReactions() {
        return null;
    }
    
    public final boolean isSystemMessage() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSystemMessageType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDeliveryStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLocalId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPriority() {
        return null;
    }
    
    public GroupMessage() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.String> getServerTimestamp() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.model.MessageType getMessageType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.model.DeliveryStatus getDeliveryStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.model.MessagePriority getPriority() {
        return null;
    }
    
    public final boolean hasAttachments() {
        return false;
    }
    
    public final boolean isSeenBy(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return false;
    }
    
    public final int getReactionCount() {
        return 0;
    }
    
    public final boolean hasUserReacted(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String emoji) {
        return false;
    }
    
    public final boolean canBeDeletedBy(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.UserRole userRole) {
        return false;
    }
    
    public final boolean canBeEditedBy(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    public final boolean component11() {
        return false;
    }
    
    public final long component12() {
        return 0L;
    }
    
    public final boolean component13() {
        return false;
    }
    
    public final long component14() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Long> component15() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.List<java.lang.String>> component16() {
        return null;
    }
    
    public final boolean component17() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component18() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component19() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component20() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component21() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    public final long component7() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.synapse.social.studioasinc.groupchat.data.model.MessageAttachment> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.synapse.social.studioasinc.groupchat.data.model.GroupMessage copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String senderId, @org.jetbrains.annotations.NotNull()
    java.lang.String senderName, @org.jetbrains.annotations.NotNull()
    java.lang.String senderPhotoUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String text, long timestamp, @org.jetbrains.annotations.NotNull()
    java.lang.String messageType, @org.jetbrains.annotations.NotNull()
    java.util.List<com.synapse.social.studioasinc.groupchat.data.model.MessageAttachment> attachments, @org.jetbrains.annotations.NotNull()
    java.lang.String replyToMessageId, boolean isEdited, long editedAt, boolean isDeleted, long deletedAt, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Long> seenBy, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, ? extends java.util.List<java.lang.String>> reactions, boolean isSystemMessage, @org.jetbrains.annotations.NotNull()
    java.lang.String systemMessageType, @org.jetbrains.annotations.NotNull()
    java.lang.String deliveryStatus, @org.jetbrains.annotations.NotNull()
    java.lang.String localId, @org.jetbrains.annotations.NotNull()
    java.lang.String priority) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}