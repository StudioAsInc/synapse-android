package com.synapse.social.studioasinc.groupchat.data.repository.impl;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010$\n\u0002\u0010\u0000\n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001B1\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0004\b\f\u0010\rJ\u0018\u0010\u0013\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u0014H\u0096@\u00a2\u0006\u0004\b\u0016\u0010\u0017J \u0010\u0018\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u00142\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0004\b\u001b\u0010\u001cJ\u0018\u0010\u001d\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u001e2\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J \u0010\u001f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u00142\u0006\u0010 \u001a\u00020\u001aH\u0096@\u00a2\u0006\u0004\b!\u0010\u001cJ \u0010\"\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00150\u00142\u0006\u0010#\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0004\b$\u0010\u001cJ\u001e\u0010%\u001a\b\u0012\u0004\u0012\u00020&0\u00142\u0006\u0010\'\u001a\u00020\u0015H\u0096@\u00a2\u0006\u0004\b(\u0010)J$\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150+0\u00142\u0006\u0010,\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0004\b-\u0010\u001cJ*\u0010.\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150+0\u00142\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u001a0+H\u0096@\u00a2\u0006\u0004\b0\u00101J\u001e\u00102\u001a\b\u0012\u0004\u0012\u00020&0\u00142\u0006\u00103\u001a\u000204H\u0096@\u00a2\u0006\u0004\b5\u00106J\u001c\u00107\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150+0\u0014H\u0096@\u00a2\u0006\u0004\b8\u0010\u0017J\u0016\u00109\u001a\b\u0012\u0004\u0012\u0002040\u001e2\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u001e\u0010:\u001a\b\u0012\u0004\u0012\u00020&0\u00142\u0006\u0010;\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0004\b<\u0010\u001cJ \u0010=\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a0\u00142\u0006\u0010\u0019\u001a\u00020\u001aH\u0096@\u00a2\u0006\u0004\b>\u0010\u001cJ\u001e\u0010?\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00142\u0006\u0010@\u001a\u00020AH\u0096@\u00a2\u0006\u0004\bB\u0010CJ\u0016\u0010D\u001a\b\u0012\u0004\u0012\u00020&0\u0014H\u0096@\u00a2\u0006\u0004\bE\u0010\u0017J*\u0010F\u001a\b\u0012\u0004\u0012\u00020&0\u00142\u0012\u0010G\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020I0HH\u0096@\u00a2\u0006\u0004\bJ\u0010KJ\"\u0010L\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020I0H0\u0014H\u0096@\u00a2\u0006\u0004\bM\u0010\u0017J\u0016\u0010N\u001a\b\u0012\u0004\u0012\u00020&0\u0014H\u0096@\u00a2\u0006\u0004\bO\u0010\u0017J\u001c\u0010P\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150+0\u0014H\u0096@\u00a2\u0006\u0004\bQ\u0010\u0017J\u000e\u0010R\u001a\u00020&H\u0086@\u00a2\u0006\u0002\u0010\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006S"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/repository/impl/UserRepositoryImpl;", "Lcom/synapse/social/studioasinc/groupchat/data/repository/UserRepository;", "userDao", "Lcom/synapse/social/studioasinc/groupchat/data/local/dao/UserDao;", "firebaseAuth", "Lcom/google/firebase/auth/FirebaseAuth;", "firebaseDatabase", "Lcom/google/firebase/database/FirebaseDatabase;", "firebaseStorage", "Lcom/google/firebase/storage/FirebaseStorage;", "firebaseMessaging", "Lcom/google/firebase/messaging/FirebaseMessaging;", "<init>", "(Lcom/synapse/social/studioasinc/groupchat/data/local/dao/UserDao;Lcom/google/firebase/auth/FirebaseAuth;Lcom/google/firebase/database/FirebaseDatabase;Lcom/google/firebase/storage/FirebaseStorage;Lcom/google/firebase/messaging/FirebaseMessaging;)V", "usersRef", "Lcom/google/firebase/database/DatabaseReference;", "onlineStatusRef", "storageRef", "Lcom/google/firebase/storage/StorageReference;", "getCurrentUser", "Lkotlin/Result;", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "getCurrentUser-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserById", "userId", "", "getUserById-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserByIdFlow", "Lkotlinx/coroutines/flow/Flow;", "getUserByUsername", "username", "getUserByUsername-gIAlu-s", "getUserByEmail", "email", "getUserByEmail-gIAlu-s", "updateUser", "", "user", "updateUser-gIAlu-s", "(Lcom/synapse/social/studioasinc/groupchat/data/model/User;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchUsers", "", "query", "searchUsers-gIAlu-s", "getUsersByIds", "userIds", "getUsersByIds-gIAlu-s", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateOnlineStatus", "isOnline", "", "updateOnlineStatus-gIAlu-s", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getOnlineUsers", "getOnlineUsers-IoAF18A", "getUserOnlineStatusFlow", "updateFcmToken", "token", "updateFcmToken-gIAlu-s", "getFcmToken", "getFcmToken-gIAlu-s", "updateProfilePhoto", "photoFile", "Ljava/io/File;", "updateProfilePhoto-gIAlu-s", "(Ljava/io/File;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteProfilePhoto", "deleteProfilePhoto-IoAF18A", "updateUserPreferences", "preferences", "", "", "updateUserPreferences-gIAlu-s", "(Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserPreferences", "getUserPreferences-IoAF18A", "syncUserData", "syncUserData-IoAF18A", "syncUserContacts", "syncUserContacts-IoAF18A", "initializePresenceTracking", "app_release"})
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