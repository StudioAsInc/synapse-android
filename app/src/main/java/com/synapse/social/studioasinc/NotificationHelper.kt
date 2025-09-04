package com.synapse.social.studioasinc

import android.util.Log
import com.onesignal.OneSignal
import org.json.JSONException
import org.json.JSONObject

object NotificationHelper {
    fun sendNotification(
        recipientUid: String,
        senderUid: String,
        message: String,
        notificationType: String,
        data: HashMap<String, String>,
        recipientOneSignalPlayerId: String?
    ) {
        if (recipientOneSignalPlayerId.isNullOrEmpty() || recipientOneSignalPlayerId == "missing_id") {
            // Don't send notification if player ID is missing
            return
        }

        try {
            val notificationContent = JSONObject()
                .put("include_player_ids", JSONObject().put("0", recipientOneSignalPlayerId))
                .put("headings", JSONObject().put("en", "New Message"))
                .put("contents", JSONObject().put("en", message))
                .put("data", JSONObject(data as Map<*, *>))

            OneSignal.postNotification(notificationContent, object : com.onesignal.OneSignal.PostNotificationResponseHandler {
                override fun onSuccess(response: JSONObject) {
                    Log.i("OneSignalExample", "postNotification Success: $response")
                }

                override fun onFailure(response: JSONObject) {
                    Log.e("OneSignalExample", "postNotification Failure: $response")
                }
            })
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
