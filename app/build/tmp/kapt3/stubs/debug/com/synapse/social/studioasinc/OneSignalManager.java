package com.synapse.social.studioasinc;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005H\u0007R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/synapse/social/studioasinc/OneSignalManager;", "", "<init>", "()V", "TAG", "", "db", "Lcom/google/firebase/database/DatabaseReference;", "savePlayerIdToRealtimeDatabase", "", "userUid", "playerId", "app_debug"})
public final class OneSignalManager {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "OneSignalManager";
    @org.jetbrains.annotations.NotNull()
    private static final com.google.firebase.database.DatabaseReference db = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.synapse.social.studioasinc.OneSignalManager INSTANCE = null;
    
    private OneSignalManager() {
        super();
    }
    
    /**
     * Saves or updates the user's OneSignal Player ID in the Firebase Realtime Database.
     * This is now the primary method for storing the player ID.
     *
     * @param userUid The Firebase UID of the user.
     * @param playerId The OneSignal Player ID to save.
     */
    @kotlin.jvm.JvmStatic()
    public static final void savePlayerIdToRealtimeDatabase(@org.jetbrains.annotations.NotNull()
    java.lang.String userUid, @org.jetbrains.annotations.NotNull()
    java.lang.String playerId) {
    }
}