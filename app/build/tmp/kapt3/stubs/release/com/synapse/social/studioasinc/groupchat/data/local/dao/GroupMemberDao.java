package com.synapse.social.studioasinc.groupchat.data.local.dao;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\f\n\u0002\u0010\t\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\'J\u001c\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\tJ \u0010\n\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\fJ \u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0007H\'J\u001c\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\tJ$\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u001c\u0010\u0017\u001a\u00020\u00142\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0016J&\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u001dJ\u001e\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\fJ&\u0010\u001f\u001a\u00020\u00142\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010 \u001a\u00020!H\u00a7@\u00a2\u0006\u0002\u0010\"J\u001e\u0010#\u001a\u00020\u00142\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010$\u001a\u00020\u00142\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u000e\u0010%\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010&\u00a8\u0006\'\u00c0\u0006\u0003"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMemberDao;", "", "getGroupMembersFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMember;", "groupId", "", "getGroupMembers", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGroupMember", "userId", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGroupMemberFlow", "getGroupAdmins", "searchGroupMembers", "query", "getGroupMemberCount", "", "insertGroupMember", "", "member", "(Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMember;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertGroupMembers", "members", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateGroupMember", "updateMemberRole", "role", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "removeMember", "updateLastSeen", "timestamp", "", "(Ljava/lang/String;Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteGroupMember", "deleteAllGroupMembers", "deleteAllMembers", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
@androidx.room.Dao()
public abstract interface GroupMemberDao {
    
    @androidx.room.Query(value = "SELECT * FROM group_members WHERE groupId = :groupId AND isActive = 1 ORDER BY role DESC, joinedAt ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMember>> getGroupMembersFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId);
    
    @androidx.room.Query(value = "SELECT * FROM group_members WHERE groupId = :groupId AND isActive = 1 ORDER BY role DESC, joinedAt ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getGroupMembers(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMember>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM group_members WHERE groupId = :groupId AND userId = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getGroupMember(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.synapse.social.studioasinc.groupchat.data.model.GroupMember> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM group_members WHERE groupId = :groupId AND userId = :userId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.synapse.social.studioasinc.groupchat.data.model.GroupMember> getGroupMemberFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId);
    
    @androidx.room.Query(value = "SELECT * FROM group_members WHERE groupId = :groupId AND role IN (\'OWNER\', \'ADMIN\') AND isActive = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getGroupAdmins(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMember>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM group_members WHERE groupId = :groupId AND userName LIKE \'%\' || :query || \'%\' AND isActive = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchGroupMembers(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMember>> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM group_members WHERE groupId = :groupId AND isActive = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getGroupMemberCount(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertGroupMember(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.GroupMember member, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertGroupMembers(@org.jetbrains.annotations.NotNull()
    java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMember> members, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateGroupMember(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.GroupMember member, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE group_members SET role = :role WHERE groupId = :groupId AND userId = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateMemberRole(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String role, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE group_members SET isActive = 0 WHERE groupId = :groupId AND userId = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object removeMember(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE group_members SET lastSeenAt = :timestamp WHERE groupId = :groupId AND userId = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLastSeen(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM group_members WHERE groupId = :groupId AND userId = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteGroupMember(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM group_members WHERE groupId = :groupId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllGroupMembers(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM group_members")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllMembers(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}