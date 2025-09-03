package com.synapse.social.studioasinc

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.synapse.social.studioasinc.util.LogToFile
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONArray
import java.io.IOException

/**
 * Enhanced notification system supporting both server-side and client-side OneSignal notifications.
 *
 * Features:
 * - Toggle between server-side (Cloudflare Workers) and client-side (OneSignal REST API) notification sending
 * - Smart notification suppression when both users are actively chatting
 * - Fallback mechanisms for reliability
 * - Configurable notification settings
 */
object NotificationHelper {

    private const val TAG = "NotificationHelper"

    private val JSON = "application/json; charset=utf-8".toMediaType()
    private const val ONESIGNAL_API_URL = "https://onesignal.com/api/v1/notifications"

    private fun showToast(context: Context, message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Sends a notification to a user.
     *
     * @param context The application context for showing toasts.
     * @param recipientUid The UID of the user to send the notification to.
     * @param senderUid The UID of the user sending the notification.
     * @param message The message to send in the notification.
     * @param notificationType The type of notification to send.
     * @param data A map of additional data to send with the notification.
     */
    @JvmStatic
    fun sendNotification(
        context: Context,
        recipientUid: String,
        senderUid: String,
        message: String,
        notificationType: String,
        data: Map<String, String>? = null
    ) {
        LogToFile.log(context, "sendNotification called for recipient: $recipientUid")
        showToast(context, "Attempting to send notification...")

        if (recipientUid == senderUid) {
            LogToFile.log(context, "Suppressed notification: recipient is the same as sender.")
            return
        }

        val userDb = FirebaseDatabase.getInstance().getReference("skyline/users")
        userDb.child(recipientUid).child("oneSignalPlayerId").get().addOnSuccessListener {
            val recipientOneSignalPlayerId = it.getValue(String::class.java)
            if (recipientOneSignalPlayerId.isNullOrBlank()) {
                Log.w(TAG, "Recipient OneSignal Player ID is blank. Cannot send notification.")
                LogToFile.log(context, "Suppressed notification: Recipient OneSignal Player ID is blank.")
                showToast(context, "Notification failed: Recipient ID not found.")
                return@addOnSuccessListener
            }
            LogToFile.log(context, "Recipient OneSignal Player ID: $recipientOneSignalPlayerId")

            val usersRef = FirebaseDatabase.getInstance().getReference("/skyline/users/$recipientUid")

            usersRef.get().addOnSuccessListener { dataSnapshot ->
                val recipientStatus = dataSnapshot.child("status").getValue(String::class.java)
                val recipientActivity = dataSnapshot.child("activity").getValue(String::class.java)
                val suppressActivity = "chatting_with_$senderUid"

                if (NotificationConfig.ENABLE_SMART_SUPPRESSION) {
                    if (suppressActivity == recipientActivity) {
                        val logMsg = "Recipient is actively chatting with sender. Suppressing notification."
                        Log.i(TAG, logMsg)
                        LogToFile.log(context, logMsg)
                        showToast(context, "Notification suppressed: User is busy.")
                        return@addOnSuccessListener
                    }

                    val lastSeen = recipientStatus?.toLongOrNull()
                    if (lastSeen != null) {
                        val now = System.currentTimeMillis()
                        if (now - lastSeen < NotificationConfig.RECENT_ACTIVITY_THRESHOLD) {
                            val logMsg = "Recipient was recently active. Suppressing notification."
                            Log.i(TAG, logMsg)
                            LogToFile.log(context, logMsg)
                            showToast(context, "Notification suppressed: User recently active.")
                            return@addOnSuccessListener
                        }
                    }
                }

                if (NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                    sendClientSideNotification(
                        context,
                        recipientOneSignalPlayerId,
                        message,
                        senderUid,
                        notificationType,
                        data
                    )
                } else {
                    sendServerSideNotification(context, recipientOneSignalPlayerId, message, notificationType, data)
                }
                saveNotificationToDatabase(context, recipientUid, senderUid, message, notificationType, data)
            }.addOnFailureListener { e ->
                Log.e(TAG, "Status check failed. Defaulting to send notification.", e)
                LogToFile.log(context, "Status check failed. Defaulting to send notification. Error: ${e.message}")
                if (NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                     sendClientSideNotification(
                        context,
                        recipientOneSignalPlayerId,
                        message,
                        senderUid,
                        notificationType,
                        data
                    )
                } else {
                    sendServerSideNotification(context, recipientOneSignalPlayerId, message, notificationType, data)
                }
                saveNotificationToDatabase(context, recipientUid, senderUid, message, notificationType, data)
            }
        }.addOnFailureListener {
            Log.e(TAG, "Failed to get recipient's OneSignal Player ID.", it)
            LogToFile.log(context, "Failed to get recipient's OneSignal Player ID. Error: ${it.message}")
            showToast(context, "Notification failed: Could not get recipient ID.")
        }
    }

    /**
     * Enhanced notification sending with smart presence checking and dual system support.
     *
     * @param senderUid The UID of the message sender
     * @param recipientUid The UID of the message recipient
     * @param recipientOneSignalPlayerId The OneSignal Player ID of the recipient
     * @param message The message content to send in the notification
     * @param chatId Optional chat ID for deep linking (can be null)
     * @deprecated Use sendNotification instead.
     */
    @JvmStatic
    @Deprecated("Use sendNotification instead.")
    fun sendMessageAndNotifyIfNeeded(
        context: Context,
        senderUid: String,
        recipientUid: String,
        recipientOneSignalPlayerId: String,
        message: String,
        chatId: String? = null
    ) {
        sendNotification(
            context,
            recipientUid,
            senderUid,
            message,
            "chat_message",
            if (chatId != null) mapOf("chat_id" to chatId) else null
        )
    }

    /**
     * Sends notification via the existing Cloudflare Worker (server-side).
     */
    @JvmStatic
    fun sendServerSideNotification(
        context: Context,
        recipientId: String,
        message: String,
        notificationType: String,
        data: Map<String, String>? = null
    ) {
        LogToFile.log(context, "Attempting to send server-side notification.")
        if (notificationType != "chat_message") {
            Log.w(TAG, "Server-side notification for type $notificationType is not yet implemented. Sending a generic message.")
        }

        val client = OkHttpClient()
        val jsonBody = JSONObject()
        try {
            jsonBody.put("recipientUserId", recipientId)
            jsonBody.put("notificationMessage", message)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create JSON for server-side notification", e)
            LogToFile.log(context, "Failed to create JSON for server-side notification. Error: ${e.message}")
            return
        }

        val body = jsonBody.toString().toRequestBody(JSON)
        val request = Request.Builder()
            .url(NotificationConfig.WORKER_URL)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to send server-side notification", e)
                LogToFile.log(context, "Failed to send server-side notification. Error: ${e.message}")
                showToast(context, "Server-side notification failed.")
                if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && !NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                    Log.i(TAG, "Falling back to client-side notification due to server failure")
                    LogToFile.log(context, "Falling back to client-side notification.")
                    sendClientSideNotification(context, recipientId, message, null, notificationType, data)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        Log.i(TAG, "Server-side notification sent successfully.")
                        LogToFile.log(context, "Server-side notification sent successfully.")
                        showToast(context, "Server-side notification sent.")
                    } else {
                        Log.e(TAG, "Failed to send server-side notification: ${it.code}")
                        LogToFile.log(context, "Failed to send server-side notification: ${it.code}")
                        showToast(context, "Server-side notification failed: ${it.code}")
                        if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && !NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                            Log.i(TAG, "Falling back to client-side notification due to server error")
                            LogToFile.log(context, "Falling back to client-side notification.")
                            sendClientSideNotification(context, recipientId, message, null, notificationType, data)
                        }
                    }
                }
            }
        })
    }

    /**
     * Sends notification directly via OneSignal REST API (client-side).
     */
    @JvmStatic
    fun sendClientSideNotification(
        context: Context,
        recipientPlayerId: String,
        message: String,
        senderUid: String? = null,
        notificationType: String,
        data: Map<String, String>? = null
    ) {
        LogToFile.log(context, "Attempting to send client-side notification.")
        val client = OkHttpClient()
        val jsonBody = JSONObject()

        try {
            jsonBody.put("app_id", NotificationConfig.ONESIGNAL_APP_ID)
            jsonBody.put("include_player_ids", JSONArray().put(recipientPlayerId))
            jsonBody.put("contents", JSONObject().put("en", message))
            jsonBody.put("headings", JSONObject().put("en", NotificationConfig.getTitleForNotificationType(notificationType)))
            jsonBody.put("subtitle", JSONObject().put("en", NotificationConfig.NOTIFICATION_SUBTITLE))

            if (NotificationConfig.ENABLE_DEEP_LINKING) {
                val dataJson = JSONObject()
                if (senderUid != null) {
                    dataJson.put("sender_uid", senderUid)
                }
                dataJson.put("type", notificationType)
                data?.forEach { (key, value) ->
                    dataJson.put(key, value)
                }
                jsonBody.put("data", dataJson)
            }

            jsonBody.put("priority", NotificationConfig.NOTIFICATION_PRIORITY)
            jsonBody.put("android_channel_id", NotificationConfig.NOTIFICATION_CHANNEL_ID)

        } catch (e: Exception) {
            Log.e(TAG, "Failed to create JSON for client-side notification", e)
            LogToFile.log(context, "Failed to create JSON for client-side notification. Error: ${e.message}")
            return
        }

        val body = jsonBody.toString().toRequestBody(JSON)
        val request = Request.Builder()
            .url(ONESIGNAL_API_URL)
            .addHeader("Authorization", "Basic ${NotificationConfig.ONESIGNAL_REST_API_KEY}")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to send client-side notification", e)
                LogToFile.log(context, "Failed to send client-side notification. Error: ${e.message}")
                showToast(context, "Client-side notification failed.")
                if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                    Log.i(TAG, "Falling back to server-side notification due to client-side failure")
                    LogToFile.log(context, "Falling back to server-side notification.")
                    sendServerSideNotification(context, recipientPlayerId, message, notificationType, data)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        Log.i(TAG, "Client-side notification sent successfully.")
                        LogToFile.log(context, "Client-side notification sent successfully.")
                        showToast(context, "Client-side notification sent.")
                    } else {
                        val errorBody = it.body?.string()
                        Log.e(TAG, "Failed to send client-side notification: ${it.code} - $errorBody")
                        LogToFile.log(context, "Failed to send client-side notification: ${it.code} - $errorBody")
                        showToast(context, "Client-side notification failed: ${it.code}")
                        if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                            Log.i(TAG, "Falling back to server-side notification due to client-side error")
                            LogToFile.log(context, "Falling back to server-side notification.")
                            sendServerSideNotification(context, recipientPlayerId, message, notificationType, data)
                        }
                    }
                }
            }
        })
    }

    /**
     * Legacy method for backward compatibility.
     * @deprecated Use sendMessageAndNotifyIfNeeded with chatId parameter instead
     */
    @JvmStatic
    @Deprecated("Use sendMessageAndNotifyIfNeeded with chatId parameter for better deep linking")
    fun triggerPushNotification(context: Context, recipientId: String, message: String) {
        @Suppress("DEPRECATION")
        sendMessageAndNotifyIfNeeded(context, "", "", recipientId, message)
    }

    /**
     * Gets the current notification system being used.
     * @return true if using client-side notifications, false if using server-side
     */
    @JvmStatic
    fun isUsingClientSideNotifications(): Boolean {
        return NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS
    }

    /**
     * Checks if the notification system is properly configured.
     * @return true if configuration is valid, false otherwise
     */
    @JvmStatic
    fun isNotificationSystemConfigured(): Boolean {
        return NotificationConfig.isConfigurationValid()
    }

    private fun saveNotificationToDatabase(
        context: Context,
        recipientUid: String,
        senderUid: String,
        message: String,
        notificationType: String,
        data: Map<String, String>?
    ) {
        LogToFile.log(context, "Saving notification to database for recipient: $recipientUid")
        val notificationsRef = FirebaseDatabase.getInstance().getReference("skyline/notifications").child(recipientUid)
        val notificationId = notificationsRef.push().key
        if (notificationId != null) {
            val notification = com.synapse.social.studioasinc.model.Notification(
                senderUid,
                message,
                notificationType,
                data?.get("postId"),
                data?.get("commentId"),
                System.currentTimeMillis()
            )
            notificationsRef.child(notificationId).setValue(notification).addOnSuccessListener {
                LogToFile.log(context, "Notification saved to database successfully.")
            }.addOnFailureListener {
                LogToFile.log(context, "Failed to save notification to database. Error: ${it.message}")
            }
        }
    }
}
