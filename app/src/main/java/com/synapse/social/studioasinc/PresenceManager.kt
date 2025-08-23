package com.synapse.social.studioasinc

import com.google.firebase.database.FirebaseDatabase

/**
 * Manages user online presence in Firebase.
 * Handles online, offline, and chat statuses.
 */
object PresenceManager {

    private val database = FirebaseDatabase.getInstance().getReference("/status")

    /**
     * Sets user status to "online".
     * Registers onDisconnect to set to "offline".
     * Call when app is in the foreground.
     * @param uid The Firebase UID of the current user.
     */
    @JvmStatic
    fun goOnline(uid: String) {
        val userStatusRef = database.child(uid)
        userStatusRef.setValue("online")
        // Set onDisconnect to mark user as offline
        userStatusRef.onDisconnect().setValue("offline")
    }

    /**
     * Explicitly sets the user's status to "offline".
     * Call on manual logout or app backgrounding.
     * @param uid The Firebase UID of the current user.
     */
    @JvmStatic
    fun goOffline(uid: String) {
        database.child(uid).setValue("offline")
    }

    /**
     * Sets status to "chatting_with_<otherUserUid>".
     * Call from ChatActivity's onResume/onStart.
     * @param currentUserUid The UID of the current user.
     * @param otherUserUid The UID of the user they are chatting with.
     */
    @JvmStatic
    fun setChattingWith(currentUserUid: String, otherUserUid: String) {
        database.child(currentUserUid).setValue("chatting_with_$otherUserUid")
    }

    /**
     * Reverts the user's status back to "online".
     * Call from ChatActivity's onPause/onStop.
     * @param currentUserUid The UID of the current user.
     */
    @JvmStatic
    fun stopChatting(currentUserUid: String) {
        // Revert to "online" if still connected.
        // onDisconnect handles "offline" on app close.
        database.child(currentUserUid).setValue("online")
    }
}
