package com.synapse.social.studioasinc.groupchat.data.repository.impl;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001B9\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u00a2\u0006\u0004\b\u000e\u0010\u000fJ\u0014\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00190\u0018H\u0016J\u0014\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u001c0\u0018H\u0016J\u001e\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001a0\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0096@\u00a2\u0006\u0004\b!\u0010\"J\u0018\u0010#\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a0\u00182\u0006\u0010\u001f\u001a\u00020 H\u0016J(\u0010$\u001a\b\u0012\u0004\u0012\u00020 0\u001e2\u0006\u0010%\u001a\u00020\u001a2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0096@\u00a2\u0006\u0004\b(\u0010)J\u001e\u0010*\u001a\b\u0012\u0004\u0012\u00020+0\u001e2\u0006\u0010%\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0004\b,\u0010-J\u001e\u0010.\u001a\b\u0012\u0004\u0012\u00020+0\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0096@\u00a2\u0006\u0004\b/\u0010\"J$\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u001c0\u001e2\u0006\u00101\u001a\u00020 H\u0096@\u00a2\u0006\u0004\b2\u0010\"J6\u00103\u001a\b\u0012\u0004\u0012\u00020+0\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u00104\u001a\u0002052\u0006\u00106\u001a\u00020 2\u0006\u00107\u001a\u00020 H\u0096@\u00a2\u0006\u0004\b8\u00109J&\u0010:\u001a\b\u0012\u0004\u0012\u00020+0\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010;\u001a\u00020 H\u0096@\u00a2\u0006\u0004\b<\u0010=J.\u0010>\u001a\b\u0012\u0004\u0012\u00020+0\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010;\u001a\u00020 2\u0006\u0010?\u001a\u00020 H\u0096@\u00a2\u0006\u0004\b@\u0010AJ$\u0010B\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020C0\u001c0\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0096@\u00a2\u0006\u0004\bD\u0010\"J\u001c\u0010E\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020C0\u001c0\u00182\u0006\u0010\u001f\u001a\u00020 H\u0016J(\u0010F\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010C0\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010;\u001a\u00020 H\u0096@\u00a2\u0006\u0004\bG\u0010=J,\u0010H\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020C0\u001c0\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u00101\u001a\u00020 H\u0096@\u00a2\u0006\u0004\bI\u0010=J&\u0010J\u001a\b\u0012\u0004\u0012\u00020 0\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010&\u001a\u00020\'H\u0096@\u00a2\u0006\u0004\bK\u0010LJ\u001e\u0010M\u001a\b\u0012\u0004\u0012\u00020+0\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0096@\u00a2\u0006\u0004\bN\u0010\"J$\u0010O\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u001c0\u001e2\u0006\u0010;\u001a\u00020 H\u0096@\u00a2\u0006\u0004\bP\u0010\"J&\u0010Q\u001a\b\u0012\u0004\u0012\u00020R0\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010;\u001a\u00020 H\u0096@\u00a2\u0006\u0004\bS\u0010=J\u001e\u0010T\u001a\b\u0012\u0004\u0012\u00020U0\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0096@\u00a2\u0006\u0004\bV\u0010\"J.\u0010W\u001a\b\u0012\u0004\u0012\u00020R0\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010;\u001a\u00020 2\u0006\u0010X\u001a\u00020 H\u0096@\u00a2\u0006\u0004\bY\u0010AJ\u0016\u0010Z\u001a\b\u0012\u0004\u0012\u00020+0\u001eH\u0096@\u00a2\u0006\u0004\b[\u0010\\J\u001e\u0010]\u001a\b\u0012\u0004\u0012\u00020+0\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0096@\u00a2\u0006\u0004\b^\u0010\"J\u0018\u0010_\u001a\b\u0012\u0004\u0012\u00020\u001a0\u001c*\u00020\u0003H\u0082@\u00a2\u0006\u0002\u0010`R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006a"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/GroupRepositoryImpl;", "Lcom/synapse/social/studioasinc/groupchat/data/repository/GroupRepository;", "groupDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupDao;", "groupMemberDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMemberDao;", "userDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/UserDao;", "firebaseAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "firebaseDatabase", "Lcom/google/firebase/database/FirebaseDatabase;", "firebaseStorage", "Lcom/google/firebase/storage/FirebaseStorage;", "<init>", "(Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupDao;Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupMemberDao;Lcom/synapse/social/studioasinc/groupchat/data/local/dao/UserDao;Lcom/google/firebase/auth/FirebaseAuth;Lcom/google/firebase/database/FirebaseDatabase;Lcom/google/firebase/storage/FirebaseStorage;)V", "groupsRef", "Lcom/google/firebase/database/DatabaseReference;", "groupMembersRef", "usersRef", "userGroupsRef", "groupsByMemberRef", "membersByGroupRef", "getAllGroupsPaged", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/synapse/social/studioasinc/groupchat/data/model/Group;", "getAllGroupsFlow", "", "getGroupById", "Lkotlin/Result;", "groupId", "", "getGroupById-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGroupByIdFlow", "createGroup", "group", "iconFile", "Ljava/io/File;", "createGroup-0E7RQCE", "(Lcom/synapse/social/studioasinc/groupchat/data/model/Group;Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateGroup", "", "updateGroup-gIAlu-s", "(Lcom/synapse/social/studioasinc/groupchat/data/model/Group;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteGroup", "deleteGroup-gIAlu-s", "searchGroups", "query", "searchGroups-gIAlu-s", "addMember", "user", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "role", "addedBy", "addMember-yxL6bBk", "(Ljava/lang/String;Lcom/synapse/social/studioasinc/groupchat/data/model/User;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "removeMember", "userId", "removeMember-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMemberRole", "newRole", "updateMemberRole-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getGroupMembers", "Lcom/synapse/social/studioasinc/groupchat/data/model/GroupMember;", "getGroupMembers-gIAlu-s", "getGroupMembersFlow", "getGroupMember", "getGroupMember-0E7RQCE", "searchGroupMembers", "searchGroupMembers-0E7RQCE", "uploadGroupIcon", "uploadGroupIcon-0E7RQCE", "(Ljava/lang/String;Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteGroupIcon", "deleteGroupIcon-gIAlu-s", "getUserGroups", "getUserGroups-gIAlu-s", "isUserMember", "", "isUserMember-0E7RQCE", "getGroupMemberCount", "", "getGroupMemberCount-gIAlu-s", "canUserPerformAction", "action", "canUserPerformAction-BWLJW6A", "syncGroups", "syncGroups-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncGroupMembers", "syncGroupMembers-gIAlu-s", "getAllGroups", "(Lcom/synapse/social/studioasinc/groupchat/data/local/dao/GroupDao;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
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