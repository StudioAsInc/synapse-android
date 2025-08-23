package com.synapse.social.studioasinc.util;

import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;

/**
 * A LiveData class that wraps a SharedPreferences value.
 * It observes changes to the preference and updates its value.
 *
 * @param <T> The type of the preference value.
 */
public abstract class PreferenceLiveData<T> extends LiveData<T> {

    protected final SharedPreferences sharedPrefs;
    protected final String key;
    protected final T defaultValue;

    private final SharedPreferences.OnSharedPreferenceChangeListener listener =
            (sharedPreferences, changedKey) -> {
                if (this.key.equals(changedKey)) {
                    setValue(getValueFromPreferences(key, defaultValue));
                }
            };

    public PreferenceLiveData(SharedPreferences sharedPrefs, String key, T defaultValue) {
        this.sharedPrefs = sharedPrefs;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    protected void onActive() {
        super.onActive();
        setValue(getValueFromPreferences(key, defaultValue));
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Retrieves the preference value from SharedPreferences.
     *
     * @param key The preference key.
     * @param defaultValue The default value if the preference is not set.
     * @return The preference value.
     */
    protected abstract T getValueFromPreferences(String key, T defaultValue);

    /**
     * A LiveData for String preferences.
     */
    public static class StringPreferenceLiveData extends PreferenceLiveData<String> {
        public StringPreferenceLiveData(SharedPreferences sharedPrefs, String key, String defaultValue) {
            super(sharedPrefs, key, defaultValue);
        }

        @Override
        protected String getValueFromPreferences(String key, String defaultValue) {
            return sharedPrefs.getString(key, defaultValue);
        }
    }

    /**
     * A LiveData for Boolean preferences.
     */
    public static class BooleanPreferenceLiveData extends PreferenceLiveData<Boolean> {
        public BooleanPreferenceLiveData(SharedPreferences sharedPrefs, String key, Boolean defaultValue) {
            super(sharedPrefs, key, defaultValue);
        }

        @Override
        protected Boolean getValueFromPreferences(String key, Boolean defaultValue) {
            return sharedPrefs.getBoolean(key, defaultValue);
        }
    }
}
