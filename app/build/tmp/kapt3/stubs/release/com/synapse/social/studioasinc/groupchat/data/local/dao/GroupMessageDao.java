package com.synapse.social.studioasinc.groupchat.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0017\n\u0002\u0010$\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u001e\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ&\u0010\u000e\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001c\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u00132\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00152\u0006\u0010\n\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u00182\u0006\u0010\n\u001a\u00020\u0005H\'J\u0018\u0010\u0019\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u001a\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J$\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00150\u001d2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u001fJ\u001c\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00150\u001d2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J&\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00150\u001d2\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\"\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010#J&\u0010$\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u001d0\u00182\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\"\u001a\u00020\u0014H\'J\u001e\u0010%\u001a\u00020\u00142\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010&\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u001fJ\u0016\u0010\'\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010)J\u001c\u0010*\u001a\u00020\u00032\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00150\u001dH\u00a7@\u00a2\u0006\u0002\u0010,J\u0016\u0010-\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J$\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00150\u001d2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010/\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u001fJ\u001e\u00100\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u00052\u0006\u00101\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u001fJ\u0016\u00102\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010)J0\u00103\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u00052\u0018\u00104\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001d05H\u00a7@\u00a2\u0006\u0002\u00106J*\u00107\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u00052\u0012\u00108\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\f05H\u00a7@\u00a2\u0006\u0002\u00106\u00a8\u00069"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMessageDao;", "", "deleteAllGroupMessages", "", "groupId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAllMessages", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteMessage", "messageId", "deletedAt", "", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "editMessage", "newText", "editedAt", "(Ljava/lang/String;Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGroupMessagesPaged", "Landroidx/paging/PagingSource;", "", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;", "getMessageById", "getMessageByIdFlow", "Lkotlinx/coroutines/flow/Flow;", "getMessageByLocalId", "localId", "getMessageCount", "getMessagesByUser", "", "userId", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPendingMessages", "getRecentMessages", "limit", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentMessagesFlow", "getUnreadMessageCount", "currentUserId", "insertMessage", "message", "(Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMessage;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertMessages", "messages", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "permanentlyDeleteMessage", "searchMessages", "query", "updateDeliveryStatus", "status", "updateMessage", "updateReactions", "reactions", "", "(Ljava/lang/String;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSeenBy", "seenBy", "app_release"})
@androidx.room.Dao()
public abstract interface GroupMessageDao {
    
    @androidx.room.Query(value = "SELECT * FROM group_messages WHERE groupId = :groupId AND isDeleted = 0 ORDER BY timestamp DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.paging.PagingSource<java.lang.Integer, com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> getGroupMessagesPaged(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId);
    
    @androidx.room.Query(value = "SELECT * FROM group_messages WHERE groupId = :groupId AND isDeleted = 0 ORDER BY timestamp DESC LIMIT :limit")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecentMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM group_messages WHERE groupId = :groupId AND isDeleted = 0 ORDER BY timestamp DESC LIMIT :limit")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage>> getRecentMessagesFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, int limit);
    
    @androidx.room.Query(value = "SELECT * FROM group_messages WHERE id = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMessageById(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM group_messages WHERE id = :messageId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> getMessageByIdFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId);
    
    @androidx.room.Query(value = "SELECT * FROM group_messages WHERE localId = :localId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMessageByLocalId(@org.jetbrains.annotations.NotNull()
    java.lang.String localId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM group_messages WHERE groupId = :groupId AND text LIKE \'%\' || :query || \'%\' AND isDeleted = 0 ORDER BY timestamp DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM group_messages WHERE groupId = :groupId AND deliveryStatus = \'SENDING\' OR deliveryStatus = \'FAILED\'")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPendingMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM group_messages WHERE groupId = :groupId AND senderId = :userId AND isDeleted = 0 ORDER BY timestamp DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMessagesByUser(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMessage(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.GroupMessage message, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertMessages(@org.jetbrains.annotations.NotNull()
    java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMessage> messages, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMessage(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.GroupMessage message, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE group_messages SET deliveryStatus = :status WHERE id = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateDeliveryStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    java.lang.String status, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE group_messages SET text = :newText, isEdited = 1, editedAt = :editedAt WHERE id = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object editMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    java.lang.String newText, long editedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE group_messages SET isDeleted = 1, deletedAt = :deletedAt WHERE id = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, long deletedAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE group_messages SET seenBy = :seenBy WHERE id = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSeenBy(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Long> seenBy, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE group_messages SET reactions = :reactions WHERE id = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateReactions(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, ? extends java.util.List<java.lang.String>> reactions, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM group_messages WHERE id = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object permanentlyDeleteMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM group_messages WHERE groupId = :groupId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllGroupMessages(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM group_messages")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllMessages(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM group_messages WHERE groupId = :groupId AND isDeleted = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMessageCount(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM group_messages WHERE groupId = :groupId AND senderId != :currentUserId AND NOT EXISTS (SELECT 1 FROM json_each(seenBy) WHERE json_each.key = :currentUserId)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUnreadMessageCount(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String currentUserId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}