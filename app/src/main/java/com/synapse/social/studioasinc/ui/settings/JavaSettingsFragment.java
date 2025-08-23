package com.synapse.social.studioasinc.ui.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.synapse.social.studioasinc.R;
import com.synapse.social.studioasinc.data.settings.SettingsDataStore;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class JavaSettingsFragment extends PreferenceFragmentCompat {

    private JavaSettingsViewModel viewModel;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey);

        viewModel = new ViewModelProvider(this).get(JavaSettingsViewModel.class);

        setupDynamicAiModelPreference();
        setupSimpleChangeListeners();
        setupNavigationListeners();
        setupStaticInfo();
    }

    private void setupDynamicAiModelPreference() {
        ListPreference aiAssistantPref = findPreference("ai_assistant");
        ListPreference aiModelPref = findPreference("ai_model");

        if (aiAssistantPref != null) {
            aiAssistantPref.setOnPreferenceChangeListener((preference, newValue) -> {
                viewModel.setAiAssistant((String) newValue);
                return true;
            });
        }

        if (aiModelPref != null) {
            aiModelPref.setOnPreferenceChangeListener((preference, newValue) -> {
                viewModel.setAiModel((String) newValue);
                return true;
            });
        }

        viewModel.getAiAssistant().observe(getViewLifecycleOwner(), assistant -> {
            if (aiModelPref == null || assistant == null) return;

            Resources res = getResources();
            int entriesId;
            int valuesId;

            switch (assistant) {
                case "gemini":
                    entriesId = R.array.settings_model_gemini_entries;
                    valuesId = R.array.settings_model_gemini_values;
                    break;
                case "chatgpt":
                    entriesId = R.array.settings_model_chatgpt_entries;
                    valuesId = R.array.settings_model_chatgpt_values;
                    break;
                case "claude":
                    entriesId = R.array.settings_model_claude_entries;
                    valuesId = R.array.settings_model_claude_values;
                    break;
                case "deepseek":
                    entriesId = R.array.settings_model_deepseek_entries;
                    valuesId = R.array.settings_model_deepseek_values;
                    break;
                default:
                    entriesId = R.array.settings_model_default_entries;
                    valuesId = R.array.settings_model_default_values;
            }

            aiModelPref.setEntries(res.getTextArray(entriesId));
            aiModelPref.setEntryValues(res.getTextArray(valuesId));

            if (aiModelPref.findIndexOfValue(aiModelPref.getValue()) == -1) {
                aiModelPref.setValueIndex(0);
            }
        });
    }

    private void setupSimpleChangeListeners() {
        ListPreference themePref = findPreference("theme");
        if (themePref != null) {
            themePref.setOnPreferenceChangeListener((preference, newValue) -> {
                viewModel.setTheme((String) newValue);
                return true;
            });
        }

        Preference privateAccountPref = findPreference("private_account");
        if (privateAccountPref != null) {
            privateAccountPref.setOnPreferenceChangeListener((preference, newValue) -> {
                viewModel.setBooleanPreference("private_account", (Boolean) newValue);
                return true;
            });
        }
    }

    private void setupNavigationListeners() {
        findPreference("edit_profile").setOnPreferenceClickListener(p -> {
            // TODO: Navigate to EditProfileActivity
            return true;
        });

        findPreference("terms_of_service").setOnPreferenceClickListener(p -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com/terms")));
            return true;
        });
         findPreference("privacy_policy").setOnPreferenceClickListener(p -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com/privacy")));
            return true;
        });
    }

    private void setupStaticInfo() {
        try {
            PackageInfo pInfo = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            Preference versionPref = findPreference("app_version");
            if (versionPref != null) {
                versionPref.setSummary(version);
            }
        } catch (Exception e) {
            Log.e("JavaSettingsFragment", "Couldn't get package info", e);
        }
    }
}
