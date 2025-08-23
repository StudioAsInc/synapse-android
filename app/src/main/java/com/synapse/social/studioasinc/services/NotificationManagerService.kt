package com.synapse.social.studioasinc.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.config.NotificationConfig
import com.synapse.social.studioasinc.ui.MainActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

/**
 * Service for managing notifications and OneSignal integration.
 * Handles creating notification channels, sending notifications, and managing OneSignal API calls.
 */
class NotificationManagerService(private val context: Context) {
    
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val client = OkHttpClient()
    
    companion object {
        private const val CHANNEL_ID_CHAT = "chat_messages"
        private const val CHANNEL_ID_GROUP = "group_updates"
        private const val CHANNEL_ID_FRIEND = "friend_requests"
        private const val CHANNEL_ID_MENTION = "mentions"
        
        private const val NOTIFICATION_ID_CHAT = 1001
        private const val NOTIFICATION_ID_GROUP = 1002
        private const val NOTIFICATION_ID_FRIEND = 1003
        private const val NOTIFICATION_ID_MENTION = 1004
    }
    
    init {
        createNotificationChannels()
    }
    
    /**
     * Creates notification channels for Android 8.0+ (API level 26+)
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_ID_CHAT,
                    "Chat Messages",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for new chat messages"
                    enableLights(true)
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_ID_GROUP,
                    "Group Updates",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications for group activities"
                    enableLights(false)
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_ID_FRIEND,
                    "Friend Requests",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications for friend requests"
                    enableLights(false)
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_ID_MENTION,
                    "Mentions",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications when you are mentioned"
                    enableLights(true)
                    enableVibration(true)
                }
            )
            
            notificationManager.createNotificationChannels(channels)
        }
    }
    
    /**
     * Shows a chat message notification
     */
    fun showChatNotification(senderName: String, message: String, chatId: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("chat_id", chatId)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_CHAT)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("New message from $senderName")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_CHAT, notification)
    }
    
    /**
     * Shows a group update notification
     */
    fun showGroupNotification(groupName: String, updateType: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_GROUP)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Group Update: $groupName")
            .setContentText(updateType)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_GROUP, notification)
    }
    
    /**
     * Shows a friend request notification
     */
    fun showFriendRequestNotification(senderName: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_FRIEND)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("New Friend Request")
            .setContentText("$senderName sent you a friend request")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_FRIEND, notification)
    }
    
    /**
     * Shows a mention notification
     */
    fun showMentionNotification(senderName: String, content: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_MENTION)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("You were mentioned by $senderName")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_MENTION, notification)
    }
    
    /**
     * Sends a notification via OneSignal API
     */
    fun sendOneSignalNotification(
        title: String,
        message: String,
        playerIds: List<String>,
        data: Map<String, String> = emptyMap()
    ) {
        if (!NotificationConfig.isApiKeyConfigured()) {
            android.util.Log.w("NotificationManager", "OneSignal API key not configured")
            return
        }
        
        val json = JSONObject().apply {
            put("app_id", NotificationConfig.ONESIGNAL_APP_ID)
            put("include_player_ids", playerIds)
            put("headings", JSONObject().put("en", title))
            put("contents", JSONObject().put("en", message))
            put("data", JSONObject(data))
        }
        
        val requestBody = json.toString().toRequestBody("application/json".toMediaType())
        
        val request = Request.Builder()
            .url(NotificationConfig.ONESIGNAL_REST_API_URL)
            .addHeader("Authorization", "Basic ${NotificationConfig.ONESIGNAL_REST_API_KEY}")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()
        
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                android.util.Log.e("NotificationManager", "Failed to send OneSignal notification", e)
            }
            
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    android.util.Log.d("NotificationManager", "OneSignal notification sent successfully")
                } else {
                    android.util.Log.e("NotificationManager", "OneSignal API error: ${response.code}")
                }
                response.close()
            }
        })
    }
    
    /**
     * Cancels all notifications
     */
    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }
    
    /**
     * Cancels a specific notification by ID
     */
    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }
}