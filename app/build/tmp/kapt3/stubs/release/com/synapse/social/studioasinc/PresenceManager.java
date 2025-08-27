package com.synapse.social.studioasinc;

/**
 * Manages user online presence in Firebase, writing to the correct database path.
 * Handles online, offline (timestamp), and chat statuses.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0002J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0007H\u0007J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0007H\u0007J\u0018\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007H\u0007J\u0010\u0010\u000e\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0007H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/synapse/social/studioasinc/PresenceManager;", "", "()V", "usersRef", "Lcom/google/firebase/database/DatabaseReference;", "getUserStatusRef", "uid", "", "goOffline", "", "goOnline", "setChattingWith", "currentUserUid", "otherUserUid", "stopChatting", "app_release"})
public final class PresenceManager {
    @org.jetbrains.annotations.NotNull()
    private static final com.google.firebase.database.DatabaseReference usersRef = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.synapse.social.studioasinc.PresenceManager INSTANCE = null;
    
    private PresenceManager() {
        super();
    }
    
    /**
     * Returns the specific database reference for a user's status.
     * Path: /skyline/users/{uid}/status
     */
    private final com.google.firebase.database.DatabaseReference getUserStatusRef(java.lang.String uid) {
        return null;
    }
    
    /**
     * Sets user status to "online".
     * Registers onDisconnect to set a timestamp for last seen.
     * @param uid The Firebase UID of the current user.
     */
    @kotlin.jvm.JvmStatic()
    public static final void goOnline(@org.jetbrains.annotations.NotNull()
    java.lang.String uid) {
    }
    
    /**
     * Explicitly sets the user's status to a timestamp (last seen).
     * @param uid The Firebase UID of the current user.
     */
    @kotlin.jvm.JvmStatic()
    public static final void goOffline(@org.jetbrains.annotations.NotNull()
    java.lang.String uid) {
    }
    
    /**
     * Sets status to "chatting_with_<otherUserUid>".
     * @param currentUserUid The UID of the current user.
     * @param otherUserUid The UID of the user they are chatting with.
     */
    @kotlin.jvm.JvmStatic()
    public static final void setChattingWith(@org.jetbrains.annotations.NotNull()
    java.lang.String currentUserUid, @org.jetbrains.annotations.NotNull()
    java.lang.String otherUserUid) {
    }
    
    /**
     * Reverts the user's status back to "online".
     * @param currentUserUid The UID of the current user.
     */
    @kotlin.jvm.JvmStatic()
    public static final void stopChatting(@org.jetbrains.annotations.NotNull()
    java.lang.String currentUserUid) {
    }
}