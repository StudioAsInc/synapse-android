package di;

import android.content.Context;

import data.settings.SettingsRepository;
import data.settings.SettingsRepositoryImpl;

// Hilt DI stub. Replace with @Module/@Provides when wiring Hilt.
public class SettingsModule {
    public static SettingsRepository provideSettingsRepository(Context context) {
        return new SettingsRepositoryImpl(context);
    }
}

