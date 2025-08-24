package com.synapse.social.studioasinc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragmentCompat extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey);

        Preference editProfile = findPreference("edit_profile");
        if (editProfile != null) editProfile.setOnPreferenceClickListener(p -> { toast(); return true; });
        Preference blocked = findPreference("privacy_blocked_users");
        if (blocked != null) blocked.setOnPreferenceClickListener(p -> { toast(); return true; });
        Preference muted = findPreference("privacy_muted_accounts");
        if (muted != null) muted.setOnPreferenceClickListener(p -> { toast(); return true; });
        Preference quiet = findPreference("notif_quiet_hours");
        if (quiet != null) quiet.setOnPreferenceClickListener(p -> { toast(); return true; });
        Preference email = findPreference("notif_email_prefs");
        if (email != null) email.setOnPreferenceClickListener(p -> { toast(); return true; });

        Preference terms = findPreference("about_terms");
        if (terms != null) terms.setOnPreferenceClickListener(p -> { openUrl("https://example.com/terms"); return true; });
        Preference privacy = findPreference("about_privacy");
        if (privacy != null) privacy.setOnPreferenceClickListener(p -> { openUrl("https://example.com/privacy"); return true; });
        Preference licenses = findPreference("about_licenses");
        if (licenses != null) licenses.setOnPreferenceClickListener(p -> { toast(); return true; });
        Preference signOut = findPreference("danger_sign_out");
        if (signOut != null) signOut.setOnPreferenceClickListener(p -> { toast(); return true; });
        Preference deleteAccount = findPreference("danger_delete_account");
        if (deleteAccount != null) deleteAccount.setOnPreferenceClickListener(p -> { toast(); return true; });

        SwitchPreferenceCompat priv = findPreference("privacy_private_account");
        if (priv != null) priv.setOnPreferenceChangeListener((preference, newValue) -> true);
        ListPreference theme = findPreference("appearance_theme");
        if (theme != null) theme.setOnPreferenceChangeListener((preference, newValue) -> true);
    }

    private void openUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void toast() {
        Toast.makeText(requireContext(), "Coming soon", Toast.LENGTH_SHORT).show();
    }
}

