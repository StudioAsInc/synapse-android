package com.synapse.social.studioasinc.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.data.settings.SettingsDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey)

        setupDynamicAiModelPreference()
        setupSimpleChangeListeners()
        setupNavigationListeners()
        setupStaticInfo()
    }

    private fun setupDynamicAiModelPreference() {
        val aiAssistantPref = findPreference<ListPreference>("ai_assistant")
        val aiModelPref = findPreference<ListPreference>("ai_model")

        // When the assistant changes, update the ViewModel
        aiAssistantPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setAiAssistant(newValue as String)
            true // True to update the state of the preference
        }

        // When the model changes, update the ViewModel
        aiModelPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setAiModel(newValue as String)
            true
        }

        // Observe the StateFlow from the ViewModel to update the UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.aiAssistant.collectLatest { assistant ->
                val entriesId = when (assistant) {
                    "gemini" -> R.array.settings_model_gemini_entries
                    "chatgpt" -> R.array.settings_model_chatgpt_entries
                    "claude" -> R.array.settings_model_claude_entries
                    "deepseek" -> R.array.settings_model_deepseek_entries
                    else -> R.array.settings_model_default_entries
                }
                val valuesId = when (assistant) {
                    "gemini" -> R.array.settings_model_gemini_values
                    "chatgpt" -> R.array.settings_model_chatgpt_values
                    "claude" -> R.array.settings_model_claude_values
                    "deepseek" -> R.array.settings_model_deepseek_values
                    else -> R.array.settings_model_default_values
                }
                aiModelPref?.entries = resources.getTextArray(entriesId)
                aiModelPref?.entryValues = resources.getTextArray(valuesId)

                // If the currently saved model value is not in the new list, reset it.
                if (aiModelPref?.findIndexOfValue(aiModelPref.value) == -1) {
                    aiModelPref?.setValueIndex(0)
                }
            }
        }
    }

    private fun setupSimpleChangeListeners() {
        findPreference<ListPreference>("theme")?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setTheme(newValue as String)
            // Note: A real theme change would likely require recreating the activity
            true
        }

        findPreference<Preference>("private_account")?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setBooleanPreference(SettingsDataStore.PrefKeys.PRIVATE_ACCOUNT, newValue as Boolean)
            true
        }
    }

    private fun setupNavigationListeners() {
        val navMap = mapOf(
            "edit_profile" to "com.synapse.social.studioasinc.ProfileEditActivity",
            "blocked_users" to "com.synapse.social.studioasinc.BlockedUsersActivity", // Stub
            "muted_accounts" to "com.synapse.social.studioasinc.MutedUsersActivity", // Stub
            "email_notifications" to "com.synapse.social.studioasinc.EmailPrefsActivity", // Stub
            "change_password" to "com.synapse.social.studioasinc.ChangePasswordActivity", // Stub
            "open_source_licenses" to "com.synapse.social.studioasinc.OpenSourceLicensesActivity" // Stub
        )

        navMap.forEach { (key, className) ->
            findPreference<Preference>(key)?.setOnPreferenceClickListener {
                try {
                    val intent = Intent(requireActivity(), Class.forName(className))
                    startActivity(intent)
                } catch (e: ClassNotFoundException) {
                    Log.e("SettingsFragment", "Activity class not found: $className", e)
                    // TODO: Show a toast or dialog to the user
                }
                true
            }
        }

        // Special case for browser intents
        findPreference<Preference>("terms_of_service")?.setOnPreferenceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com/terms")))
            true
        }
        findPreference<Preference>("privacy_policy")?.setOnPreferenceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com/privacy")))
            true
        }
    }

    private fun setupStaticInfo() {
        try {
            val pInfo = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            val version = pInfo.versionName
            findPreference<Preference>("app_version")?.summary = version
        } catch (e: Exception) {
            findPreference<Preference>("app_version")?.summary = "Unknown"
        }
    }
}
