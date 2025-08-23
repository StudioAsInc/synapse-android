package com.synapse.social.studioasinc

import com.google.firebase.database.FirebaseDatabase

/**
 * A singleton object to manage the user's online presence in Firebase Realtime Database.
 * This manager handles setting the status to online, offline, and tracking chat activity.
 */
object PresenceManager {

    private val database = FirebaseDatabase.getInstance().getReference("/status")

    /**
     * Sets the user's status to "online" and registers an `onDisconnect` handler
     * to set them to "offline" when the app connection is lost.
     *
     * This should be called when the user brings the app to the foreground.
     *
     * @param uid The Firebase Authentication UID of the current user.
     */
    fun goOnline(uid: String) {
        val userStatusRef = database.child(uid)
        userStatusRef.setValue("online")
        // Set the onDisconnect handler to mark the user as offline
        userStatusRef.onDisconnect().setValue("offline")
    }

    /**
     * Explicitly sets the user's status to "offline".
     *
     * This can be called when the user manually logs out or brings the app to the background.
     *
     * @param uid The Firebase Authentication UID of the current user.
     */
    fun goOffline(uid: String) {
        database.child(uid).setValue("offline")
    }

    /**
     * Sets the user's status to indicate they are chatting with another user.
     * The status will be "chatting_with_<otherUserUid>".
     *
     * This should be called from the `onResume` or `onStart` of ChatActivity.
     *
     * @param currentUserUid The Firebase UID of the current user.
     * @param otherUserUid The Firebase UID of the user they are chatting with.
     */
    fun setChattingWith(currentUserUid: String, otherUserUid: String) {
        database.child(currentUserUid).setValue("chatting_with_$otherUserUid")
    }

    /**
     * Reverts the user's status from "chatting_with_..." back to "online".
     *
     * This should be called from the `onPause` or `onStop` of ChatActivity,
     * so that the user is considered "online" but not in that specific chat anymore.
     *
     * @param currentUserUid The Firebase UID of the current user.
     */
    fun stopChatting(currentUserUid: String) {
        // We only revert to "online" if they are still connected.
        // The onDisconnect handler will take care of the "offline" status if the app closes.
        database.child(currentUserUid).setValue("online")
    }
}
