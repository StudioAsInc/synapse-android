package com.synapse.social.studioasinc.ui.settings

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.services.NotificationManagerService
import com.synapse.social.studioasinc.utils.NotificationPreferencesManager

class NotificationTestActivity : AppCompatActivity() {
    
    private lateinit var notificationManager: NotificationManagerService
    private lateinit var preferencesManager: NotificationPreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_test)
        
        // Initialize services
        notificationManager = NotificationManagerService(this)
        preferencesManager = NotificationPreferencesManager(this)
        
        // Setup toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Test Notifications"
        
        // Setup UI
        setupUI()
    }
    
    private fun setupUI() {
        // Test chat notification button
        findViewById<MaterialButton>(R.id.btn_test_chat).setOnClickListener {
            if (preferencesManager.shouldShowNotification(NotificationPreferencesManager.NotificationType.CHAT_MESSAGE)) {
                notificationManager.showChatNotification("John Doe", "Hey! How are you doing?", "chat_123")
                showToast("Chat notification sent!")
            } else {
                showToast("Chat notifications are disabled")
            }
        }
        
        // Test group notification button
        findViewById<MaterialButton>(R.id.btn_test_group).setOnClickListener {
            if (preferencesManager.shouldShowNotification(NotificationPreferencesManager.NotificationType.GROUP_UPDATE)) {
                notificationManager.showGroupNotification("Tech Enthusiasts", "New member joined the group")
                showToast("Group notification sent!")
            } else {
                showToast("Group notifications are disabled")
            }
        }
        
        // Test friend request button
        findViewById<MaterialButton>(R.id.btn_test_friend).setOnClickListener {
            if (preferencesManager.shouldShowNotification(NotificationPreferencesManager.NotificationType.FRIEND_REQUEST)) {
                notificationManager.showFriendRequestNotification("Jane Smith")
                showToast("Friend request notification sent!")
            } else {
                showToast("Friend request notifications are disabled")
            }
        }
        
        // Test mention button
        findViewById<MaterialButton>(R.id.btn_test_mention).setOnClickListener {
            if (preferencesManager.shouldShowNotification(NotificationPreferencesManager.NotificationType.MENTION)) {
                notificationManager.showMentionNotification("Alex Johnson", "Hey @you, check out this post!")
                showToast("Mention notification sent!")
            } else {
                showToast("Mention notifications are disabled")
            }
        }
        
        // Test OneSignal button
        findViewById<MaterialButton>(R.id.btn_test_onesignal).setOnClickListener {
            notificationManager.sendOneSignalNotification(
                "Test Notification",
                "This is a test notification from OneSignal",
                listOf("test_player_id"),
                mapOf("type" to "test", "action" to "open_app")
            )
            showToast("OneSignal notification sent!")
        }
        
        // Update status display
        updateStatusDisplay()
    }
    
    private fun updateStatusDisplay() {
        val statusText = findViewById<MaterialTextView>(R.id.tv_notification_status)
        val summary = preferencesManager.getNotificationSettingsSummary()
        statusText.text = "Current Status: $summary"
        
        // Update quiet hours status
        val quietHoursText = findViewById<MaterialTextView>(R.id.tv_quiet_hours_status)
        val quietHoursStatus = if (preferencesManager.isCurrentlyQuietHours()) {
            "🔇 Quiet Hours Active (${preferencesManager.getQuietHoursStart()}:00 - ${preferencesManager.getQuietHoursEnd()}:00)"
        } else {
            "🔊 Notifications Active"
        }
        quietHoursText.text = quietHoursStatus
    }
    
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}