package com.synapse.social.studioasinc.data.settings;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.synapse.social.studioasinc.util.PreferenceLiveData;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JavaSettingsRepositoryImpl implements JavaSettingsRepository {

    private static final String TAG = "JavaSettingsRepo";

    private final SharedPreferences prefs;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth auth;

    // Preference Keys (must match preferences_settings.xml)
    private static final String KEY_THEME = "theme";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_PRIVATE_ACCOUNT = "private_account";
    private static final String KEY_PROFILE_VISIBILITY = "profile_visibility";
    private static final String KEY_AI_ASSISTANT = "ai_assistant";
    private static final String KEY_AI_MODEL = "ai_model";


    @Inject
    public JavaSettingsRepositoryImpl(SharedPreferences sharedPreferences, FirebaseFirestore firestore, FirebaseAuth auth) {
        this.prefs = sharedPreferences;
        this.firestore = firestore;
        this.auth = auth;
    }

    private void updateFirestore(String key, Object value) {
        if (auth.getCurrentUser() != null) {
            String uid = auth.getCurrentUser().getUid();
            Map<String, Object> data = new HashMap<>();
            data.put(key, value);
            firestore.collection("user_settings").document(uid)
                .set(data, SetOptions.merge())
                .addOnFailureListener(e -> Log.e(TAG, "Failed to update Firestore", e));
        }
    }

    @Override
    public LiveData<String> getTheme() {
        return new PreferenceLiveData.StringPreferenceLiveData(prefs, KEY_THEME, "system");
    }

    @Override
    public void setTheme(String theme) {
        prefs.edit().putString(KEY_THEME, theme).apply();
        updateFirestore(KEY_THEME, theme);
    }

    @Override
    public LiveData<String> getFontSize() {
        return new PreferenceLiveData.StringPreferenceLiveData(prefs, KEY_FONT_SIZE, "normal");
    }

    @Override
    public void setFontSize(String fontSize) {
        prefs.edit().putString(KEY_FONT_SIZE, fontSize).apply();
        updateFirestore(KEY_FONT_SIZE, fontSize);
    }

    @Override
    public LiveData<Boolean> isPrivateAccount() {
        return getBooleanPreference(KEY_PRIVATE_ACCOUNT, false);
    }

    @Override
    public void setPrivateAccount(boolean isPrivate) {
        setBooleanPreference(KEY_PRIVATE_ACCOUNT, isPrivate);
    }

    @Override
    public LiveData<String> getProfileVisibility() {
        return new PreferenceLiveData.StringPreferenceLiveData(prefs, KEY_PROFILE_VISIBILITY, "public");
    }

    @Override
    public void setProfileVisibility(String visibility) {
        prefs.edit().putString(KEY_PROFILE_VISIBILITY, visibility).apply();
        updateFirestore(KEY_PROFILE_VISIBILITY, visibility);
    }

    @Override
    public LiveData<String> getAiAssistant() {
        return new PreferenceLiveData.StringPreferenceLiveData(prefs, KEY_AI_ASSISTANT, "gemini");
    }

    @Override
    public void setAiAssistant(String assistant) {
        prefs.edit().putString(KEY_AI_ASSISTANT, assistant).apply();
        updateFirestore(KEY_AI_ASSISTANT, assistant);
    }

    @Override
    public LiveData<String> getAiModel() {
        return new PreferenceLiveData.StringPreferenceLiveData(prefs, KEY_AI_MODEL, "default");
    }

    @Override
    public void setAiModel(String model) {
        prefs.edit().putString(KEY_AI_MODEL, model).apply();
        updateFirestore(KEY_AI_MODEL, model);
    }

    @Override
    public LiveData<Boolean> getBooleanPreference(String key, boolean defaultValue) {
        return new PreferenceLiveData.BooleanPreferenceLiveData(prefs, key, defaultValue);
    }

    @Override
    public void setBooleanPreference(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
        updateFirestore(key, value);
    }
}
