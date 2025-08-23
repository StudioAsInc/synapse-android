package com.synapse.social.studioasinc

import android.util.Log
import com.google.firebase.database.FirebaseDatabase

/**
 * A utility object to manage OneSignal integration,
 * such as saving the player ID to the Realtime Database.
 */
object OneSignalManager {

    private const val TAG = "OneSignalManager"

    /**
     * Saves or updates the user's OneSignal Player ID in the Realtime Database.
     *
     * @param uid The Firebase Authentication UID of the user.
     * @param playerId The OneSignal Player ID to save.
     */
    @JvmStatic
    fun savePlayerIdToRtdb(uid: String, playerId: String) {
        if (uid.isEmpty() || playerId.isEmpty()) {
            Log.w(TAG, "UID or Player ID is empty. Cannot save.")
            return
        }

        // Save to /users/{uid}/oneSignalPlayerId
        FirebaseDatabase.getInstance().getReference("users")
            .child(uid)
            .child("oneSignalPlayerId")
            .setValue(playerId)
            .addOnSuccessListener { Log.d(TAG, "OneSignal Player ID saved to RTDB for user: $uid") }
            .addOnFailureListener { e -> Log.e(TAG, "Error saving OneSignal Player ID to RTDB", e) }
    }
}
