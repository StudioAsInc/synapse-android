package com.synapse.social.studioasinc.groupchat.data.repository.impl;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0002\u0010\u0000\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0015\u0010\u0016J\u001e\u0010\u0017\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\u0013H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0019\u0010\u0016J&\u0010\u001a\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u00132\u0006\u0010\u001c\u001a\u00020\u001bH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001d\u0010\u001eJ\"\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180 0\u0013H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b!\u0010\u0016J&\u0010\"\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\u00132\u0006\u0010#\u001a\u00020\u001bH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b$\u0010\u001eJ&\u0010%\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\u00132\u0006\u0010\u001c\u001a\u00020\u001bH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b&\u0010\u001eJ\u0018\u0010\'\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180(2\u0006\u0010\u001c\u001a\u00020\u001bH\u0016J&\u0010)\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00180\u00132\u0006\u0010*\u001a\u00020\u001bH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b+\u0010\u001eJ\u0016\u0010,\u001a\b\u0012\u0004\u0012\u00020-0(2\u0006\u0010\u001c\u001a\u00020\u001bH\u0016J(\u0010.\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001b\u0012\u0004\u0012\u0002000/0\u0013H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b1\u0010\u0016J0\u00102\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180 0\u00132\f\u00103\u001a\b\u0012\u0004\u0012\u00020\u001b0 H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b4\u00105J\u000e\u00106\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0016J*\u00107\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180 0\u00132\u0006\u00108\u001a\u00020\u001bH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b9\u0010\u001eJ\"\u0010:\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180 0\u0013H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b;\u0010\u0016J\u001c\u0010<\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b=\u0010\u0016J$\u0010>\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0006\u0010?\u001a\u00020\u001bH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b@\u0010\u001eJ$\u0010A\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0006\u0010B\u001a\u00020-H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bC\u0010DJ$\u0010E\u001a\b\u0012\u0004\u0012\u00020\u001b0\u00132\u0006\u0010F\u001a\u00020GH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bH\u0010IJ$\u0010J\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0006\u0010K\u001a\u00020\u0018H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bL\u0010MJ0\u0010N\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0012\u0010O\u001a\u000e\u0012\u0004\u0012\u00020\u001b\u0012\u0004\u0012\u0002000/H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bP\u0010QR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006R"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/UserRepositoryImpl;", "Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;", "userDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/UserDao;", "firebaseAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "firebaseDatabase", "Lcom/google/firebase/database/FirebaseDatabase;", "firebaseStorage", "Lcom/google/firebase/storage/FirebaseStorage;", "firebaseMessaging", "Lcom/google/firebase/messaging/FirebaseMessaging;", "(Lcom/synapse/social/studioasinc/groupchat/data/local/dao/UserDao;Lcom/google/firebase/auth/FirebaseAuth;Lcom/google/firebase/database/FirebaseDatabase;Lcom/google/firebase/storage/FirebaseStorage;Lcom/google/firebase/messaging/FirebaseMessaging;)V", "onlineStatusRef", "Lcom/google/firebase/database/DatabaseReference;", "storageRef", "Lcom/google/firebase/storage/StorageReference;", "usersRef", "deleteProfilePhoto", "Lkotlin/Result;", "", "deleteProfilePhoto-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCurrentUser", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "getCurrentUser-IoAF18A", "getFcmToken", "", "userId", "getFcmToken-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getOnlineUsers", "", "getOnlineUsers-IoAF18A", "getUserByEmail", "email", "getUserByEmail-gIAlu-s", "getUserById", "getUserById-gIAlu-s", "getUserByIdFlow", "Lkotlinx/coroutines/flow/Flow;", "getUserByUsername", "username", "getUserByUsername-gIAlu-s", "getUserOnlineStatusFlow", "", "getUserPreferences", "", "", "getUserPreferences-IoAF18A", "getUsersByIds", "userIds", "getUsersByIds-gIAlu-s", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "initializePresenceTracking", "searchUsers", "query", "searchUsers-gIAlu-s", "syncUserContacts", "syncUserContacts-IoAF18A", "syncUserData", "syncUserData-IoAF18A", "updateFcmToken", "token", "updateFcmToken-gIAlu-s", "updateOnlineStatus", "isOnline", "updateOnlineStatus-gIAlu-s", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateProfilePhoto", "photoFile", "Ljava/io/File;", "updateProfilePhoto-gIAlu-s", "(Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateUser", "user", "updateUser-gIAlu-s", "(Lcom/synapse/social/studioasinc/groupchat/data/model/User;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateUserPreferences", "preferences", "updateUserPreferences-gIAlu-s", "(Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public final class UserRepositoryImpl implements com.synapse.social.studioasinc.groupchat.data.repository.UserRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao userDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.auth.FirebaseAuth firebaseAuth = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.FirebaseDatabase firebaseDatabase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.storage.FirebaseStorage firebaseStorage = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.messaging.FirebaseMessaging firebaseMessaging = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.DatabaseReference usersRef = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.database.DatabaseReference onlineStatusRef = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.storage.StorageReference storageRef = null;
    
    @javax.inject.Inject()
    public UserRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.local.dao.UserDao userDao, @org.jetbrains.annotations.NotNull()
    com.google.firebase.auth.FirebaseAuth firebaseAuth, @org.jetbrains.annotations.NotNull()
    com.google.firebase.database.FirebaseDatabase firebaseDatabase, @org.jetbrains.annotations.NotNull()
    com.google.firebase.storage.FirebaseStorage firebaseStorage, @org.jetbrains.annotations.NotNull()
    com.google.firebase.messaging.FirebaseMessaging firebaseMessaging) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.synapse.social.studioasinc.groupchat.data.model.User> getUserByIdFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.lang.Boolean> getUserOnlineStatusFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return null;
    }
    
    /**
     * Initialize user presence tracking
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object initializePresenceTracking(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}