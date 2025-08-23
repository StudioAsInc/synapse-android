package com.synapse.social.studioasinc.ui.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceScreen
import com.google.android.material.appbar.MaterialToolbar
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.config.NotificationConfig

class NotificationSettingsActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)
        
        // Setup toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notification Settings"
        
        // Load settings fragment
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, NotificationSettingsFragment())
                .commit()
        }
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    
    class NotificationSettingsFragment : PreferenceFragmentCompat() {
        
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.notification_preferences, rootKey)
            
            // Setup notification preferences
            setupNotificationPreferences()
        }
        
        private fun setupNotificationPreferences() {
            // Get preferences
            val pushNotificationsPref = findPreference<SwitchPreferenceCompat>("push_notifications")
            val chatMessagesPref = findPreference<SwitchPreferenceCompat>("chat_messages")
            val groupUpdatesPref = findPreference<SwitchPreferenceCompat>("group_updates")
            val friendRequestsPref = findPreference<SwitchPreferenceCompat>("friend_requests")
            val soundPref = findPreference<SwitchPreferenceCompat>("notification_sound")
            val vibrationPref = findPreference<SwitchPreferenceCompat>("notification_vibration")
            val ledPref = findPreference<SwitchPreferenceCompat>("notification_led")
            
            // Setup OneSignal status
            val oneSignalStatusPref = findPreference<Preference>("onesignal_status")
            oneSignalStatusPref?.summary = if (NotificationConfig.isApiKeyConfigured()) {
                "✅ OneSignal is properly configured"
            } else {
                "⚠️ OneSignal needs configuration"
            }
            
            // Setup push notifications preference
            pushNotificationsPref?.setOnPreferenceChangeListener { _, newValue ->
                val enabled = newValue as Boolean
                // Enable/disable all other notification preferences
                chatMessagesPref?.isEnabled = enabled
                groupUpdatesPref?.isEnabled = enabled
                friendRequestsPref?.isEnabled = enabled
                soundPref?.isEnabled = enabled
                vibrationPref?.isEnabled = enabled
                ledPref?.isEnabled = enabled
                true
            }
            
            // Setup chat messages preference
            chatMessagesPref?.setOnPreferenceChangeListener { _, newValue ->
                val enabled = newValue as Boolean
                if (enabled) {
                    // Create notification channel for chat messages
                    createChatNotificationChannel()
                }
                true
            }
        }
        
        private fun createChatNotificationChannel() {
            // This would typically create the notification channel
            // For now, we'll just log it
            android.util.Log.d("NotificationSettings", "Creating chat notification channel")
        }
    }
}