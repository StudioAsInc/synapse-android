package com.synapse.social.studioasinc.groupchat.data.local.dao;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010\t\n\u0002\b\u000b\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003H\'J\u0014\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\b0\u0007H\'J\u0018\u0010\t\u001a\u0004\u0018\u00010\u00052\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0018\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00072\u0006\u0010\n\u001a\u00020\u000bH\'J\u001c\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\b2\u0006\u0010\u000f\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u001c\u0010\u0014\u001a\u00020\u00112\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u0016\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0013J6\u0010\u0018\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u001eJ\u001e\u0010\u001f\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010#\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u000e\u0010$\u001a\u00020\u0011H\u00a7@\u00a2\u0006\u0002\u0010%J\u000e\u0010&\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010%\u00a8\u0006\'\u00c0\u0006\u0003"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupDao;", "", "getAllGroupsPaged", "Landroidx/paging/PagingSource;", "", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "getAllGroupsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getGroupById", "groupId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGroupByIdFlow", "searchGroups", "query", "insertGroup", "", "group", "(Lcom/synapse/social/studioasinc/groupchat/data/model/Group;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertGroups", "groups", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateGroup", "updateLastMessage", "messageId", "messageText", "messageTime", "", "sender", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMemberCount", "count", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markGroupAsInactive", "deleteGroup", "deleteAllGroups", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveGroupCount", "app_release"})
@androidx.room.Dao()
public abstract interface GroupDao {
    
    @androidx.room.Query(value = "SELECT * FROM groups WHERE isActive = 1 ORDER BY lastMessageTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.paging.PagingSource<java.lang.Integer, com.synapse.social.studioasinc.groupchat.data.model.Group> getAllGroupsPaged();
    
    @androidx.room.Query(value = "SELECT * FROM groups WHERE isActive = 1 ORDER BY lastMessageTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.Group>> getAllGroupsFlow();
    
    @androidx.room.Query(value = "SELECT * FROM groups WHERE id = :groupId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getGroupById(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.synapse.social.studioasinc.groupchat.data.model.Group> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM groups WHERE id = :groupId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.synapse.social.studioasinc.groupchat.data.model.Group> getGroupByIdFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId);
    
    @androidx.room.Query(value = "SELECT * FROM groups WHERE name LIKE \'%\' || :query || \'%\' AND isActive = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchGroups(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.Group>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertGroup(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.Group group, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertGroups(@org.jetbrains.annotations.NotNull()
    java.util.List<com.synapse.social.studioasinc.groupchat.data.model.Group> groups, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateGroup(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.Group group, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE groups SET lastMessageId = :messageId, lastMessageText = :messageText, lastMessageTime = :messageTime, lastMessageSender = :sender WHERE id = :groupId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLastMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    java.lang.String messageText, long messageTime, @org.jetbrains.annotations.NotNull()
    java.lang.String sender, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE groups SET memberCount = :count WHERE id = :groupId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMemberCount(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, int count, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE groups SET isActive = 0 WHERE id = :groupId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markGroupAsInactive(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM groups WHERE id = :groupId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteGroup(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM groups")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllGroups(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM groups WHERE isActive = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getActiveGroupCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
}