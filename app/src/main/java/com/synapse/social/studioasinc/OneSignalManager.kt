package com.synapse.social.studioasinc

import android.util.Log
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
}
