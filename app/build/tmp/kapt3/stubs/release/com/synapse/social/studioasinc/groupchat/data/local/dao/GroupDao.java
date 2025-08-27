package com.synapse.social.studioasinc.groupchat.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\t\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\fH\'J\u0014\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000e0\u0010H\'J\u0018\u0010\u0011\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\f2\u0006\u0010\u0006\u001a\u00020\u0007H\'J\u0016\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0015J\u001c\u0010\u0016\u001a\u00020\u00032\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u00a7@\u00a2\u0006\u0002\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u001c\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010\u001b\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0015J6\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010#J\u001e\u0010$\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010%\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010&\u00a8\u0006\'"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupDao;", "", "deleteAllGroups", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteGroup", "groupId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveGroupCount", "", "getAllGroupsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "getAllGroupsPaged", "Landroidx/paging/PagingSource;", "getGroupById", "getGroupByIdFlow", "insertGroup", "group", "(Lcom/synapse/social/studioasinc/groupchat/data/model/Group;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertGroups", "groups", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markGroupAsInactive", "searchGroups", "query", "updateGroup", "updateLastMessage", "messageId", "messageText", "messageTime", "", "sender", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMemberCount", "count", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
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