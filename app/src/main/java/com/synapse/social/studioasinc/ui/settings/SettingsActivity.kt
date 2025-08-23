package com.synapse.social.studioasinc.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceScreen
import com.google.android.material.appbar.MaterialToolbar
import com.synapse.social.studioasinc.R

class SettingsActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        // Setup toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        
        // Load settings fragment
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, MainSettingsFragment())
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
    
    class MainSettingsFragment : PreferenceFragmentCompat() {
        
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.main_settings, rootKey)
            
            // Setup preference click listeners
            setupPreferenceClickListeners()
        }
        
        private fun setupPreferenceClickListeners() {
            // Notification settings
            val notificationPref = findPreference<Preference>("notifications")
            notificationPref?.setOnPreferenceClickListener {
                val intent = Intent(requireContext(), NotificationSettingsActivity::class.java)
                startActivity(intent)
                true
            }
            
            // Account settings
            val accountPref = findPreference<Preference>("account")
            accountPref?.setOnPreferenceClickListener {
                // Navigate to account settings
                true
            }
            
            // Privacy settings
            val privacyPref = findPreference<Preference>("privacy")
            privacyPref?.setOnPreferenceClickListener {
                // Navigate to privacy settings
                true
            }
            
            // App settings
            val appPref = findPreference<Preference>("app_settings")
            appPref?.setOnPreferenceClickListener {
                // Navigate to app settings
                true
            }
            
            // About
            val aboutPref = findPreference<Preference>("about")
            aboutPref?.setOnPreferenceClickListener {
                // Navigate to about page
                true
            }
        }
    }
}