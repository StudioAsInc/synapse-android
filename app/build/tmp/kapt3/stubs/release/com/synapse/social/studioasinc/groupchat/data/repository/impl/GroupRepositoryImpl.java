package com.synapse.social.studioasinc.groupchat.data.repository.impl;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b \b\u0007\u0018\u00002\u00020\u0001B7\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000eJ<\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001f\u0010 J4\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b%\u0010&J.\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00172\u0006\u0010(\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010+H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b,\u0010-J$\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b/\u00100J$\u00101\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b2\u00100J\u0014\u00103\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020)0504H\u0016J\u0014\u00106\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020)0704H\u0016J$\u00108\u001a\b\u0012\u0004\u0012\u00020)0\u00172\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b9\u00100J\u0018\u0010:\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010)042\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J.\u0010;\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010<0\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b=\u0010>J$\u0010?\u001a\b\u0012\u0004\u0012\u00020@0\u00172\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bA\u00100J*\u0010B\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020<050\u00172\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bC\u00100J\u001c\u0010D\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020<05042\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J*\u0010E\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020)050\u00172\u0006\u0010#\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bF\u00100J,\u0010G\u001a\b\u0012\u0004\u0012\u00020\"0\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bH\u0010>J,\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bJ\u0010>J2\u0010K\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020<050\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010L\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bM\u0010>J*\u0010N\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020)050\u00172\u0006\u0010L\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bO\u00100J$\u0010P\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bQ\u00100J\u001c\u0010R\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bS\u0010TJ$\u0010U\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010(\u001a\u00020)H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bV\u0010WJ4\u0010X\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020\u001a2\u0006\u0010Y\u001a\u00020\u001aH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bZ\u0010&J,\u0010[\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00172\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010*\u001a\u00020+H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\\\u0010]J\u0018\u0010^\u001a\b\u0012\u0004\u0012\u00020)05*\u00020\u0003H\u0082@\u00a2\u0006\u0002\u0010_R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006`"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/GroupRepositoryImpl;", "Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;", "groupDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupDao;", "groupMemberDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMemberDao;", "userDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/UserDao;", "firebaseAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "firebaseDatabase", "Lcom/google/firebase/database/FirebaseDatabase;", "firebaseStorage", "Lcom/google/firebase/storage/FirebaseStorage;", "(Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupDao;Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMemberDao;Lcom/synapse/social/studioasinc/groupchat/data/local/dao/UserDao;Lcom/google/firebase/auth/FirebaseAuth;Lcom/google/firebase/database/FirebaseDatabase;Lcom/google/firebase/storage/FirebaseStorage;)V", "groupMembersRef", "Lcom/google/firebase/database/DatabaseReference;", "groupsByMemberRef", "groupsRef", "membersByGroupRef", "userGroupsRef", "usersRef", "addMember", "Lkotlin/Result;", "", "groupId", "", "user", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "role", "addedBy", "addMember-yxL6bBk", "(Ljava/lang/String;Lcom/synapse/social/studioasinc/groupchat/data/model/User;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "canUserPerformAction", "", "userId", "action", "canUserPerformAction-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createGroup", "group", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "iconFile", "Ljava/io/File;", "createGroup-0E7RQCE", "(Lcom/synapse/social/studioasinc/groupchat/data/model/Group;Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteGroup", "deleteGroup-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteGroupIcon", "deleteGroupIcon-gIAlu-s", "getAllGroupsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getAllGroupsPaged", "Landroidx/paging/PagingData;", "getGroupById", "getGroupById-gIAlu-s", "getGroupByIdFlow", "getGroupMember", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMember;", "getGroupMember-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGroupMemberCount", "", "getGroupMemberCount-gIAlu-s", "getGroupMembers", "getGroupMembers-gIAlu-s", "getGroupMembersFlow", "getUserGroups", "getUserGroups-gIAlu-s", "isUserMember", "isUserMember-0E7RQCE", "removeMember", "removeMember-0E7RQCE", "searchGroupMembers", "query", "searchGroupMembers-0E7RQCE", "searchGroups", "searchGroups-gIAlu-s", "syncGroupMembers", "syncGroupMembers-gIAlu-s", "syncGroups", "syncGroups-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateGroup", "updateGroup-gIAlu-s", "(Lcom/synapse/social/studioasinc/groupchat/data/model/Group;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMemberRole", "newRole", "updateMemberRole-BWLJW6A", "uploadGroupIcon", "uploadGroupIcon-0E7RQCE", "(Ljava/lang/String;Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllGroups", "(Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupDao;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public final class GroupRepositoryImpl implements com.synapse.social.studioasinc.groupchat.data.repository.GroupRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao groupDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao groupMemberDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao userDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.auth.FirebaseAuth firebaseAuth = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.FirebaseDatabase firebaseDatabase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.storage.FirebaseStorage firebaseStorage = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.DatabaseReference groupsRef = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.DatabaseReference groupMembersRef = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.DatabaseReference usersRef = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.DatabaseReference userGroupsRef = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.DatabaseReference groupsByMemberRef = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.DatabaseReference membersByGroupRef = null;
    
    @javax.inject.Inject()
    public GroupRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao groupDao, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.local.dao.GroupMemberDao groupMemberDao, @org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao userDao, @org.jetbrains.annotations.NotNull()
    com.google.firebase.auth.FirebaseAuth firebaseAuth, @org.jetbrains.annotations.NotNull()
    com.google.firebase.database.FirebaseDatabase firebaseDatabase, @org.jetbrains.annotations.NotNull()
    com.google.firebase.storage.FirebaseStorage firebaseStorage) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.synapse.social.studioasinc.groupchat.data.model.Group>> getAllGroupsPaged() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.Group>> getAllGroupsFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.synapse.social.studioasinc.groupchat.data.model.Group> getGroupByIdFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.GroupMember>> getGroupMembersFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String groupId) {
        return null;
    }
    
    private final java.lang.Object getAllGroups(com.synapse.social.studioasinc.groupchat.data.local.dao.GroupDao $this$getAllGroups, kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.Group>> $completion) {
        return null;
    }
}