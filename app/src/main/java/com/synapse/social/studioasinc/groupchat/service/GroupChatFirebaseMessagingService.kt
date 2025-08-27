package com.synapse.social.studioasinc.groupchat.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.groupchat.data.repository.UserRepository
import com.synapse.social.studioasinc.groupchat.presentation.ui.GroupChatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class GroupChatFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var userRepository: UserRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private const val CHANNEL_ID = "group_chat_messages"
        private const val CHANNEL_NAME = "Group Chat Messages"
        private const val NOTIFICATION_ID_BASE = 1000
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
        // Update token in repository
        serviceScope.launch {
            try {
                userRepository.updateFcmToken(token)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains data payload
        remoteMessage.data.isNotEmpty().let {
            handleDataMessage(remoteMessage.data)
        }

        // Check if message contains notification payload
        remoteMessage.notification?.let {
            handleNotificationMessage(it, remoteMessage.data)
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val messageType = data["type"] ?: return
        
        when (messageType) {
            "group_message" -> handleGroupMessage(data)
            "group_invite" -> handleGroupInvite(data)
            "group_member_added" -> handleMemberAdded(data)
            "group_member_removed" -> handleMemberRemoved(data)
            "group_role_changed" -> handleRoleChanged(data)
            else -> {
                // Handle unknown message type
            }
        }
    }

    private fun handleNotificationMessage(
        notification: RemoteMessage.Notification,
        data: Map<String, String>
    ) {
        val title = notification.title ?: "New Message"
        val body = notification.body ?: ""
        
        showNotification(
            title = title,
            message = body,
            groupId = data["groupId"],
            groupName = data["groupName"],
            senderId = data["senderId"],
            senderName = data["senderName"],
            senderPhotoUrl = data["senderPhotoUrl"]
        )
    }

    private fun handleGroupMessage(data: Map<String, String>) {
        val groupId = data["groupId"] ?: return
        val groupName = data["groupName"] ?: "Group Chat"
        val senderId = data["senderId"] ?: return
        val senderName = data["senderName"] ?: "Unknown"
        val senderPhotoUrl = data["senderPhotoUrl"]
        val messageText = data["messageText"] ?: ""
        val messageType = data["messageType"] ?: "TEXT"

        val title = when (messageType) {
            "IMAGE" -> "$senderName sent a photo"
            "VIDEO" -> "$senderName sent a video"
            "AUDIO" -> "$senderName sent an audio message"
            "FILE" -> "$senderName sent a file"
            else -> senderName
        }

        val message = if (messageType == "TEXT") messageText else "Attachment"

        showNotification(
            title = title,
            message = message,
            groupId = groupId,
            groupName = groupName,
            senderId = senderId,
            senderName = senderName,
            senderPhotoUrl = senderPhotoUrl
        )
    }

    private fun handleGroupInvite(data: Map<String, String>) {
        val groupName = data["groupName"] ?: "Group"
        val inviterName = data["inviterName"] ?: "Someone"
        
        showNotification(
            title = "Group Invitation",
            message = "$inviterName invited you to join $groupName",
            groupId = data["groupId"],
            groupName = groupName,
            senderId = data["inviterId"],
            senderName = inviterName
        )
    }

    private fun handleMemberAdded(data: Map<String, String>) {
        val groupName = data["groupName"] ?: "Group"
        val memberName = data["memberName"] ?: "Someone"
        val addedBy = data["addedBy"] ?: "Admin"
        
        showNotification(
            title = groupName,
            message = "$addedBy added $memberName to the group",
            groupId = data["groupId"],
            groupName = groupName,
            isSystemMessage = true
        )
    }

    private fun handleMemberRemoved(data: Map<String, String>) {
        val groupName = data["groupName"] ?: "Group"
        val memberName = data["memberName"] ?: "Someone"
        
        showNotification(
            title = groupName,
            message = "$memberName left the group",
            groupId = data["groupId"],
            groupName = groupName,
            isSystemMessage = true
        )
    }

    private fun handleRoleChanged(data: Map<String, String>) {
        val groupName = data["groupName"] ?: "Group"
        val memberName = data["memberName"] ?: "Someone"
        val newRole = data["newRole"] ?: "Member"
        
        showNotification(
            title = groupName,
            message = "$memberName is now a $newRole",
            groupId = data["groupId"],
            groupName = groupName,
            isSystemMessage = true
        )
    }

    private fun showNotification(
        title: String,
        message: String,
        groupId: String?,
        groupName: String?,
        senderId: String? = null,
        senderName: String? = null,
        senderPhotoUrl: String? = null,
        isSystemMessage: Boolean = false
    ) {
        createNotificationChannel()

        val intent = if (groupId != null) {
            Intent(this, GroupChatActivity::class.java).apply {
                putExtra("GROUP_ID", groupId)
                putExtra("GROUP_NAME", groupName ?: "Group Chat")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        } else {
            packageManager.getLaunchIntentForPackage(packageName)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            groupId?.hashCode() ?: 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_group)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        // Add group-specific styling
        if (!isSystemMessage && groupId != null) {
            notificationBuilder.setGroup(groupId)
            
            // Load sender avatar if available
            if (senderPhotoUrl != null) {
                serviceScope.launch {
                    try {
                        val bitmap = Glide.with(this@GroupChatFirebaseMessagingService)
                            .asBitmap()
                            .load(senderPhotoUrl)
                            .submit()
                            .get()
                        
                        val person = Person.Builder()
                            .setName(senderName ?: "Unknown")
                            .setIcon(IconCompat.createWithBitmap(bitmap))
                            .build()

                        val messagingStyle = NotificationCompat.MessagingStyle(person)
                            .setGroupConversation(true)
                            .setConversationTitle(groupName)
                            .addMessage(message, System.currentTimeMillis(), person)

                        notificationBuilder.setStyle(messagingStyle)
                        
                        showNotificationInternal(notificationBuilder, groupId)
                    } catch (e: Exception) {
                        // Fallback to simple notification
                        showNotificationInternal(notificationBuilder, groupId)
                    }
                }
                return
            } else {
                // Create person without avatar
                val person = Person.Builder()
                    .setName(senderName ?: "Unknown")
                    .build()

                val messagingStyle = NotificationCompat.MessagingStyle(person)
                    .setGroupConversation(true)
                    .setConversationTitle(groupName)
                    .addMessage(message, System.currentTimeMillis(), person)

                notificationBuilder.setStyle(messagingStyle)
            }
        }

        showNotificationInternal(notificationBuilder, groupId)
    }

    private fun showNotificationInternal(
        notificationBuilder: NotificationCompat.Builder,
        groupId: String?
    ) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = groupId?.hashCode() ?: NOTIFICATION_ID_BASE
        
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for group chat messages"
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}