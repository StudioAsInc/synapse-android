package util;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class PreferenceLiveData<T> extends LiveData<T> implements SharedPreferences.OnSharedPreferenceChangeListener {

    public interface Getter<T> {
        T get(SharedPreferences prefs, String key, T defaultValue);
    }

    private final SharedPreferences prefs;
    private final String key;
    private final T defaultValue;
    private final Getter<T> getter;

    public PreferenceLiveData(@NonNull SharedPreferences prefs,
                              @NonNull String key,
                              T defaultValue,
                              @NonNull Getter<T> getter) {
        this.prefs = prefs;
        this.key = key;
        this.defaultValue = defaultValue;
        this.getter = getter;
        setValue(getter.get(prefs, key, defaultValue));
    }

    @Override
    protected void onActive() {
        super.onActive();
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onInactive() {
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        super.onInactive();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String changedKey) {
        if (key.equals(changedKey)) {
            setValue(getter.get(prefs, key, defaultValue));
        }
    }
}

