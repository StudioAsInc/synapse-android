package com.synapse.social.studioasinc

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

/**
 * A utility object to manage OneSignal integration,
 * such as saving the player ID to Firestore.
 */
object OneSignalManager {

    private const val TAG = "OneSignalManager"

    /**
     * Saves or updates the user's OneSignal Player ID in their Firestore document.
     * This uses SetOptions.merge() to avoid overwriting other user data.
     *
     * @param uid The Firebase Authentication UID of the user.
     * @param playerId The OneSignal Player ID to save.
     */
    @JvmStatic
    fun savePlayerIdToFirestore(uid: String, playerId: String) {
        if (uid.isEmpty() || playerId.isEmpty()) {
            Log.w(TAG, "UID or Player ID is empty. Cannot save.")
            return
        }

        val userDocRef = FirebaseFirestore.getInstance().collection("users").document(uid)
        val data = mapOf("oneSignalPlayerId" to playerId)

        userDocRef.set(data, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "OneSignal Player ID saved for user: $uid") }
            .addOnFailureListener { e -> Log.e(TAG, "Error saving OneSignal Player ID", e) }
    }
}
