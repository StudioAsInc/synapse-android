package com.synapse.social.studioasinc

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
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
    
    /**
     * Sends a notification to a user.
     *
     * @param recipientUid The UID of the user to send the notification to.
     * @param senderUid The UID of the user sending the notification.
     * @param message The message to send in the notification.
     * @param notificationType The type of notification to send.
     * @param data A map of additional data to send with the notification.
     */
    @JvmStatic
    fun sendNotification(
        recipientUid: String,
        senderUid: String,
        message: String,
        notificationType: String,
        recipientOneSignalPlayerId: String?,
        data: Map<String, String>? = null
    ) {
        if (recipientUid == senderUid) {
            // Don't send notification to self
            return
        }

        if (recipientOneSignalPlayerId.isNullOrBlank()) {
            Log.w(TAG, "Recipient OneSignal Player ID is blank. Cannot send notification.")
            return
        }

        val recipientStatusRef = FirebaseDatabase.getInstance().getReference("/skyline/users/$recipientUid/status")

        recipientStatusRef.get().addOnSuccessListener { dataSnapshot ->
            val recipientStatus = dataSnapshot.getValue(String::class.java)
            val suppressStatus = "chatting_with_$senderUid"

            if (NotificationConfig.ENABLE_SMART_SUPPRESSION) {
                if (suppressStatus == recipientStatus) {
                    if (NotificationConfig.ENABLE_DEBUG_LOGGING) {
                        Log.i(TAG, "Recipient is actively chatting with sender. Suppressing notification.")
                    }
                    return@addOnSuccessListener
                }

                if (recipientStatus == "online") {
                    if (NotificationConfig.ENABLE_DEBUG_LOGGING) {
                        Log.i(TAG, "Recipient is online. Suppressing notification for real-time message visibility.")
                    }
                    return@addOnSuccessListener
                }

                // Check for recent activity based on timestamp
                val lastSeen = recipientStatus?.toLongOrNull()
                if (lastSeen != null) {
                    val now = System.currentTimeMillis()
                    if (now - lastSeen < NotificationConfig.RECENT_ACTIVITY_THRESHOLD) {
                        if (NotificationConfig.ENABLE_DEBUG_LOGGING) {
                            Log.i(TAG, "Recipient was recently active. Suppressing notification.")
                        }
                        return@addOnSuccessListener
                    }
                }
            }

            if (NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                sendClientSideNotification(
                    recipientOneSignalPlayerId,
                    message,
                    senderUid,
                    notificationType,
                    data
                )
            } else {
                sendServerSideNotification(recipientOneSignalPlayerId, message, notificationType, data)
            }
            saveNotificationToDatabase(recipientUid, senderUid, message, notificationType, data)
        }.addOnFailureListener { e ->
            Log.e(TAG, "Status check failed. Defaulting to send notification.", e)
            if (NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                 sendClientSideNotification(
                    recipientOneSignalPlayerId,
                    message,
                    senderUid,
                    notificationType,
                    data
                )
            } else {
                sendServerSideNotification(recipientOneSignalPlayerId, message, notificationType, data)
            }
            saveNotificationToDatabase(recipientUid, senderUid, message, notificationType, data)
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
        senderUid: String,
        recipientUid: String,
        recipientOneSignalPlayerId: String,
        message: String,
        chatId: String? = null
    ) {
        sendNotification(
            recipientUid,
            senderUid,
            message,
            "chat_message",
            recipientOneSignalPlayerId,
            if (chatId != null) mapOf("chat_id" to chatId) else null
        )
    }

    /**
     * Sends notification via the existing Cloudflare Worker (server-side).
     */
    @JvmStatic
    fun sendServerSideNotification(
        recipientId: String,
        message: String,
        notificationType: String,
        data: Map<String, String>? = null
    ) {
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
                if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && !NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                    Log.i(TAG, "Falling back to client-side notification due to server failure")
                    sendClientSideNotification(recipientId, message, null, notificationType, data)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        Log.i(TAG, "Server-side notification sent successfully.")
                    } else {
                        Log.e(TAG, "Failed to send server-side notification: ${it.code}")
                        if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && !NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                            Log.i(TAG, "Falling back to client-side notification due to server error")
                            sendClientSideNotification(recipientId, message, null, notificationType, data)
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
        recipientPlayerId: String,
        message: String,
        senderUid: String? = null,
        notificationType: String,
        data: Map<String, String>? = null
    ) {
        val client = OkHttpClient()
        val jsonBody = JSONObject()
        
        try {
            jsonBody.put("app_id", NotificationConfig.ONESIGNAL_APP_ID)
            jsonBody.put("include_player_ids", JSONObject().put("0", recipientPlayerId))
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
                if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                    Log.i(TAG, "Falling back to server-side notification due to client-side failure")
                    sendServerSideNotification(recipientPlayerId, message, notificationType, data)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        Log.i(TAG, "Client-side notification sent successfully.")
                    } else {
                        Log.e(TAG, "Failed to send client-side notification: ${it.code} - ${it.body?.string()}")
                        if (NotificationConfig.ENABLE_FALLBACK_MECHANISMS && NotificationConfig.USE_CLIENT_SIDE_NOTIFICATIONS) {
                            Log.i(TAG, "Falling back to server-side notification due to client-side error")
                            sendServerSideNotification(recipientPlayerId, message, notificationType, data)
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
    fun triggerPushNotification(recipientId: String, message: String) {
        @Suppress("DEPRECATION")
        sendMessageAndNotifyIfNeeded("", "", recipientId, message)
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
        recipientUid: String,
        senderUid: String,
        message: String,
        notificationType: String,
        data: Map<String, String>?
    ) {
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
            notificationsRef.child(notificationId).setValue(notification)
        }
    }
}
