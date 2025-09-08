package com.synapse.social.studioasinc;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.chat_settings_chat, rootKey);
        addPreferencesFromResource(R.xml.chat_settings_appearance);
        addPreferencesFromResource(R.xml.chat_settings_media);
        addPreferencesFromResource(R.xml.chat_settings_security);
        addPreferencesFromResource(R.xml.chat_settings_theming);
        addPreferencesFromResource(R.xml.chat_settings_storage);
        addPreferencesFromResource(R.xml.chat_settings_ai);
    }
}
