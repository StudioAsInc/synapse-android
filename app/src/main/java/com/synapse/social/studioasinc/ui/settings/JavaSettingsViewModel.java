package com.synapse.social.studioasinc.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.synapse.social.studioasinc.data.settings.JavaSettingsRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class JavaSettingsViewModel extends ViewModel {

    private final JavaSettingsRepository repository;

    // LiveData for the UI
    private final LiveData<String> theme;
    private final LiveData<String> aiAssistant;
    private final LiveData<Boolean> isPrivateAccount;
    private final LiveData<String> profileVisibility;

    @Inject
    public JavaSettingsViewModel(JavaSettingsRepository repository) {
        this.repository = repository;

        // Initialize LiveData from the repository
        this.theme = repository.getTheme();
        this.aiAssistant = repository.getAiAssistant();
        this.isPrivateAccount = repository.isPrivateAccount();
        this.profileVisibility = repository.getProfileVisibility();
    }

    // --- Getters for the UI to observe ---

    public LiveData<String> getTheme() {
        return theme;
    }

    public LiveData<String> getAiAssistant() {
        return aiAssistant;
    }

    public LiveData<Boolean> isPrivateAccount() {
        return isPrivateAccount;
    }

    public LiveData<String> getProfileVisibility() {
        return profileVisibility;
    }

    // --- Methods for the UI to call to update settings ---

    public void setTheme(String theme) {
        repository.setTheme(theme);
    }

    public void setAiAssistant(String assistant) {
        repository.setAiAssistant(assistant);
    }

    public void setAiModel(String model) {
        repository.setAiModel(model);
    }

    public void setPrivateAccount(boolean isPrivate) {
        repository.setPrivateAccount(isPrivate);
    }

    public void setProfileVisibility(String visibility) {
        repository.setProfileVisibility(visibility);
    }

    public void setBooleanPreference(String key, boolean value) {
        repository.setBooleanPreference(key, value);
    }
}
