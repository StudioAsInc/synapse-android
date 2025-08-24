package ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import your.package.name.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsViewModel viewModel;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        Preference editProfile = findPreference("edit_profile");
        if (editProfile != null) {
            editProfile.setOnPreferenceClickListener(preference -> {
                startActivity(new Intent(requireContext(), EditProfileActivity.class));
                return true;
            });
        }

        Preference blocked = findPreference("privacy_blocked_users");
        if (blocked != null) blocked.setOnPreferenceClickListener(p -> { stub(); return true; });
        Preference muted = findPreference("privacy_muted_accounts");
        if (muted != null) muted.setOnPreferenceClickListener(p -> { stub(); return true; });

        Preference quiet = findPreference("notif_quiet_hours");
        if (quiet != null) quiet.setOnPreferenceClickListener(p -> { stub(); return true; });

        Preference emailPrefs = findPreference("notif_email_prefs");
        if (emailPrefs != null) emailPrefs.setOnPreferenceClickListener(p -> { stub(); return true; });

        Preference changePassword = findPreference("security_change_password");
        if (changePassword != null) changePassword.setOnPreferenceClickListener(p -> { stub(); return true; });

        Preference terms = findPreference("about_terms");
        if (terms != null) terms.setOnPreferenceClickListener(p -> { openUrl("https://example.com/terms"); return true; });
        Preference privacy = findPreference("about_privacy");
        if (privacy != null) privacy.setOnPreferenceClickListener(p -> { openUrl("https://example.com/privacy"); return true; });

        Preference licenses = findPreference("about_licenses");
        if (licenses != null) licenses.setOnPreferenceClickListener(p -> { stub(); return true; });

        Preference signOut = findPreference("danger_sign_out");
        if (signOut != null) signOut.setOnPreferenceClickListener(p -> { stub(); return true; });
        Preference deleteAccount = findPreference("danger_delete_account");
        if (deleteAccount != null) deleteAccount.setOnPreferenceClickListener(p -> { stub(); return true; });

        SwitchPreferenceCompat privateAccount = findPreference("privacy_private_account");
        if (privateAccount != null) {
            privateAccount.setChecked(viewModel.getPrivateAccount().getValue() != null && viewModel.getPrivateAccount().getValue());
            privateAccount.setOnPreferenceChangeListener((preference, newValue) -> {
                viewModel.setPrivateAccount((Boolean) newValue);
                return true;
            });
            viewModel.getPrivateAccount().observe(getViewLifecycleOwner(), privateAccount::setChecked);
        }

        ListPreference theme = findPreference("appearance_theme");
        if (theme != null) {
            theme.setValue(viewModel.getThemeMode().getValue());
            theme.setOnPreferenceChangeListener((preference, newValue) -> {
                viewModel.setThemeMode((String) newValue);
                return true;
            });
            viewModel.getThemeMode().observe(getViewLifecycleOwner(), theme::setValue);
        }
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void stub() {
        Toast.makeText(requireContext(), getString(R.string.toast_stub), Toast.LENGTH_SHORT).show();
    }
}

