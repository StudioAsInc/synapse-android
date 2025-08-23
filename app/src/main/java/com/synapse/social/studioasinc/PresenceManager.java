package com.synapse.social.studioasinc;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A utility class to manage the user's online presence in Firebase.
 * This class uses static methods and is not meant to be instantiated.
 */
public final class PresenceManager {

    private static final DatabaseReference database = FirebaseDatabase.getInstance().getReference("/status");

    // Private constructor to prevent instantiation.
    private PresenceManager() {}

    /**
     * Sets user status to "online" and registers onDisconnect to set to "offline".
     * Call when the app is brought to the foreground.
     * @param uid The Firebase UID of the current user.
     */
    public static void goOnline(String uid) {
        if (uid == null || uid.isEmpty()) return;
        DatabaseReference userStatusRef = database.child(uid);
        userStatusRef.setValue("online");
        // Set onDisconnect to mark the user as offline.
        userStatusRef.onDisconnect().setValue("offline");
    }

    /**
     * Explicitly sets the user's status to "offline".
     * Call on manual logout or when the app is backgrounded.
     * @param uid The Firebase UID of the current user.
     */
    public static void goOffline(String uid) {
        if (uid == null || uid.isEmpty()) return;
        database.child(uid).setValue("offline");
    }

    /**
     * Sets status to "chatting_with_<otherUserUid>".
     * Call from ChatActivity's onResume or onStart.
     * @param currentUserUid The UID of the current user.
     * @param otherUserUid The UID of the user they are chatting with.
     */
    public static void setChattingWith(String currentUserUid, String otherUserUid) {
        if (currentUserUid == null || currentUserUid.isEmpty()) return;
        database.child(currentUserUid).setValue("chatting_with_" + otherUserUid);
    }

    /**
     * Reverts the user's status back to "online".
     * Call from ChatActivity's onPause or onStop.
     * @param currentUserUid The UID of the current user.
     */
    public static void stopChatting(String currentUserUid) {
        if (currentUserUid == null || currentUserUid.isEmpty()) return;
        // Revert to "online" if the user is still connected.
        // The onDisconnect handler will manage "offline" on app close.
        database.child(currentUserUid).setValue("online");
    }
}
