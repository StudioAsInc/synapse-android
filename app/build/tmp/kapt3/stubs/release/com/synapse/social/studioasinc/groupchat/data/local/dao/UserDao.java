package com.synapse.social.studioasinc.groupchat.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\rH\'J\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u000f\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0010\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\u0011\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\r2\u0006\u0010\u0006\u001a\u00020\u0007H\'J\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0014\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\"\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00070\nH\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u001aJ\u001c\u0010\u001b\u001a\u00020\u00032\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u00a7@\u00a2\u0006\u0002\u0010\u0017J\u001c\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\u001e\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u001e\u0010\u001f\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010!J&\u0010\"\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&H\u00a7@\u00a2\u0006\u0002\u0010\'J\u001e\u0010(\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010)\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010!J\u0016\u0010*\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u001a\u00a8\u0006+"}, d2 = {"Lcom/synapse/social/studioasinc/groupchat/data/local/dao/UserDao;", "", "deleteAllUsers", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteUser", "userId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllUsers", "", "Lcom/synapse/social/studioasinc/groupchat/data/model/User;", "getAllUsersFlow", "Lkotlinx/coroutines/flow/Flow;", "getOnlineUsers", "getUserByEmail", "email", "getUserById", "getUserByIdFlow", "getUserByUsername", "username", "getUsersByIds", "userIds", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertUser", "user", "(Lcom/synapse/social/studioasinc/groupchat/data/model/User;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertUsers", "users", "searchUsers", "query", "updateFcmToken", "token", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateOnlineStatus", "isOnline", "", "lastSeen", "", "(Ljava/lang/String;ZJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updatePhotoUrl", "photoUrl", "updateUser", "app_release"})
@androidx.room.Dao()
public abstract interface UserDao {
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE isActive = 1 ORDER BY displayName ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.synapse.social.studioasinc.groupchat.data.model.User>> getAllUsersFlow();
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE isActive = 1 ORDER BY displayName ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllUsers(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.User>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE id = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUserById(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.synapse.social.studioasinc.groupchat.data.model.User> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE id = :userId")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.synapse.social.studioasinc.groupchat.data.model.User> getUserByIdFlow(@org.jetbrains.annotations.NotNull()
    java.lang.String userId);
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE username = :username")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUserByUsername(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.synapse.social.studioasinc.groupchat.data.model.User> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE email = :email")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUserByEmail(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.synapse.social.studioasinc.groupchat.data.model.User> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE (displayName LIKE \'%\' || :query || \'%\' OR username LIKE \'%\' || :query || \'%\' OR email LIKE \'%\' || :query || \'%\') AND isActive = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchUsers(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.User>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE id IN (:userIds)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUsersByIds(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> userIds, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.User>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM users WHERE isOnline = 1 AND isActive = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getOnlineUsers(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.synapse.social.studioasinc.groupchat.data.model.User>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertUser(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.User user, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertUsers(@org.jetbrains.annotations.NotNull()
    java.util.List<com.synapse.social.studioasinc.groupchat.data.model.User> users, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateUser(@org.jetbrains.annotations.NotNull()
    com.synapse.social.studioasinc.groupchat.data.model.User user, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE users SET isOnline = :isOnline, lastSeen = :lastSeen WHERE id = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateOnlineStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, boolean isOnline, long lastSeen, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE users SET fcmToken = :token WHERE id = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateFcmToken(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE users SET photoUrl = :photoUrl WHERE id = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updatePhotoUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String photoUrl, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM users WHERE id = :userId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM users")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllUsers(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}