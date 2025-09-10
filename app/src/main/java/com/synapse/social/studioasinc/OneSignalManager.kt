package com.synapse.social.studioasinc

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.onesignal.user.state.IPushSubscriptionObserver
import com.onesignal.user.state.PushSubscriptionChangedState
import com.onesignal.user.state.PushSubscriptionState

object OneSignalManager {

    private const val TAG = "OneSignalManager"

    /**
     * Initializes the OneSignal manager, setting up observers to handle push subscription changes.
     * This should be called from the Application's `onCreate` method.
     */
    @JvmStatic
    fun initialize() {
        // Add a listener for push subscription changes.
        OneSignal.User.pushSubscription.addObserver(object : IPushSubscriptionObserver {
            override fun onPushSubscriptionChange(state: PushSubscriptionChangedState) {
                if (state.current.id != null) {
                    val playerId = state.current.id
                    Log.i(TAG, "OneSignal Player ID updated: $playerId")
                    savePlayerIdToFirebase(playerId)
                } else {
                    Log.w(TAG, "OneSignal Player ID is null.")
                }
            }
        })
    }

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

        // After logging in, immediately try to get the current player ID and save it.
        val currentPlayerId = OneSignal.User.pushSubscription.id
        if (currentPlayerId != null) {
            Log.i(TAG, "Found existing OneSignal Player ID on login: $currentPlayerId")
            savePlayerIdToFirebase(currentPlayerId)
        } else {
            Log.i(TAG, "OneSignal Player ID not immediately available on login. Waiting for observer.")
        }
    }

    /**
     * Saves the provided OneSignal Player ID to the current user's profile in Firebase.
     *
     * @param playerId The OneSignal Player ID to save.
     */
    private fun savePlayerIdToFirebase(playerId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Log.w(TAG, "Cannot save Player ID: No user is currently logged in.")
            return
        }

        val userUid = currentUser.uid
        val userDbRef = FirebaseDatabase.getInstance().getReference("skyline/users").child(userUid)

        userDbRef.child("oneSignalPlayerId").setValue(playerId)
            .addOnSuccessListener {
                Log.i(TAG, "Successfully saved OneSignal Player ID to Firebase for user $userUid")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to save OneSignal Player ID to Firebase for user $userUid", e)
            }
    }
}
