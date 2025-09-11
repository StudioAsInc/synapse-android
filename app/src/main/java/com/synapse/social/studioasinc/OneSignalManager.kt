package com.synapse.social.studioasinc

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.onesignal.OneSignal

object OneSignalManager {

    private const val TAG = "OneSignalManager"

    /**
     * Logs the user into OneSignal using their Firebase UID as the External ID.
     * This is the recommended approach for OneSignal v5+ to identify users.
     *
     * @param userUid The Firebase UID of the user.
     */
    @JvmStatic
    fun loginUser(userUid: String) {
        if (userUid.isBlank()) {
            Log.w(TAG, "User UID is blank. Aborting OneSignal login.")
            return
        }

        OneSignal.login(userUid)
        Log.i(TAG, "OneSignal login called for user: $userUid")
    }

    /**
     * Saves the OneSignal Player ID to the Firebase Realtime Database.
     *
     * @param userUid The Firebase UID of the user.
     * @param playerId The OneSignal Player ID.
     */
    @JvmStatic
    fun savePlayerIdToRealtimeDatabase(userUid: String, playerId: String) {
        if (userUid.isBlank() || playerId.isBlank()) {
            Log.w(TAG, "User UID or Player ID is blank. Aborting save.")
            return
        }

        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("skyline/users").child(userUid)
        userRef.child("oneSignalPlayerId").setValue(playerId)
            .addOnSuccessListener {
                Log.i(TAG, "OneSignal Player ID saved to Realtime Database for user: $userUid")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error saving OneSignal Player ID to Realtime Database for user: $userUid", e)
            }
    }
}
