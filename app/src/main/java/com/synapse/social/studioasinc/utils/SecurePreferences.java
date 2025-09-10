package com.synapse.social.studioasinc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecurePreferences {

    private static final String PREFS_NAME = "secure_prefs";
    private static final String TAG = "SecurePreferences";

    public static SharedPreferences getEncryptedSharedPreferences(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            return EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            Log.e(TAG, "Failed to create EncryptedSharedPreferences, falling back to standard SharedPreferences.", e);
            // Fallback to clear corrupted preferences
            clearPreferences(context);
            // After clearing, you might want to re-throw or handle the error in a way
            // that the app can gracefully recover or prompt the user for re-authentication.
            // For simplicity, we'll return null and let the caller handle it.
            return null;
        }
    }

    private static void clearPreferences(Context context) {
        try {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().apply();
            Log.d(TAG, "Cleared corrupted SharedPreferences.");
        } catch (Exception e) {
            Log.e(TAG, "Failed to clear SharedPreferences.", e);
        }
    }
}
