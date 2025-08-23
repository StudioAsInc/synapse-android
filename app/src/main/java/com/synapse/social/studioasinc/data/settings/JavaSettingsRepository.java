package com.synapse.social.studioasinc.data.settings;

import androidx.lifecycle.LiveData;

/**
 * Interface for the settings data layer, tailored for the Java implementation using LiveData.
 */
public interface JavaSettingsRepository {

    // --- Appearance ---
    LiveData<String> getTheme();
    void setTheme(String theme);

    LiveData<String> getFontSize();
    void setFontSize(String fontSize);

    // --- Privacy ---
    LiveData<Boolean> isPrivateAccount();
    void setPrivateAccount(boolean isPrivate);

    LiveData<String> getProfileVisibility();
    void setProfileVisibility(String visibility);

    // --- AI & API ---
    LiveData<String> getAiAssistant();
    void setAiAssistant(String assistant);

    LiveData<String> getAiModel();
    void setAiModel(String model);

    // --- Generic Accessors for simple preferences ---

    /**
     * Gets a LiveData for any boolean preference.
     * @param key The preference key (string).
     * @param defaultValue The default value.
     */
    LiveData<Boolean> getBooleanPreference(String key, boolean defaultValue);

    /**
     * Sets the value for any boolean preference.
     * @param key The preference key (string).
     * @param value The new value.
     */
    void setBooleanPreference(String key, boolean value);
}
