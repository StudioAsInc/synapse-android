package ui.settings;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import data.settings.SettingsRepository;
import data.settings.SettingsRepositoryImpl;

public class SettingsViewModel extends AndroidViewModel {

    private final SettingsRepository repository;

    private final MutableLiveData<Boolean> privateAccount = new MutableLiveData<>();
    private final MutableLiveData<String> themeMode = new MutableLiveData<>();

    public SettingsViewModel(@NonNull Application application) {
        this(application, new SettingsRepositoryImpl(application.getApplicationContext()));
    }

    public SettingsViewModel(@NonNull Application application, @NonNull SettingsRepository repository) {
        super(application);
        this.repository = repository;
        privateAccount.setValue(repository.isPrivateAccount());
        themeMode.setValue(repository.getThemeMode());
    }

    public LiveData<Boolean> getPrivateAccount() { return privateAccount; }
    public LiveData<String> getThemeMode() { return themeMode; }

    public void setPrivateAccount(boolean enabled) {
        repository.setPrivateAccount(enabled);
        privateAccount.setValue(enabled);
    }

    public void setThemeMode(String mode) {
        repository.setThemeMode(mode);
        themeMode.setValue(mode);
    }
}

