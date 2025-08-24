package ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import your.package.name.R

class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey)

        findPreference<Preference>("edit_profile")?.setOnPreferenceClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
            true
        }

        findPreference<Preference>("privacy_blocked_users")?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), getString(R.string.toast_stub), Toast.LENGTH_SHORT).show()
            true
        }

        findPreference<Preference>("privacy_muted_accounts")?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), getString(R.string.toast_stub), Toast.LENGTH_SHORT).show()
            true
        }

        findPreference<Preference>("notif_quiet_hours")?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), getString(R.string.toast_stub), Toast.LENGTH_SHORT).show()
            true
        }

        findPreference<Preference>("notif_email_prefs")?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), getString(R.string.toast_stub), Toast.LENGTH_SHORT).show()
            true
        }

        findPreference<Preference>("security_change_password")?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), getString(R.string.toast_stub), Toast.LENGTH_SHORT).show()
            true
        }

        findPreference<Preference>("about_terms")?.setOnPreferenceClickListener {
            openUrl("https://example.com/terms")
            true
        }
        findPreference<Preference>("about_privacy")?.setOnPreferenceClickListener {
            openUrl("https://example.com/privacy")
            true
        }

        findPreference<Preference>("about_licenses")?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), getString(R.string.toast_stub), Toast.LENGTH_SHORT).show()
            true
        }

        findPreference<Preference>("danger_sign_out")?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), getString(R.string.toast_stub), Toast.LENGTH_SHORT).show()
            true
        }
        findPreference<Preference>("danger_delete_account")?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), getString(R.string.toast_stub), Toast.LENGTH_SHORT).show()
            true
        }

        // Examples wiring preferences to VM later
        findPreference<SwitchPreferenceCompat>("privacy_private_account")?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setPrivateAccount(newValue as Boolean)
            true
        }

        findPreference<ListPreference>("appearance_theme")?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setThemeMode(newValue as String)
            true
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}

