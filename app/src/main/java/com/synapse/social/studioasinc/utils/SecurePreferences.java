package com.synapse.social.studioasinc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SecurePreferences {

    private static final String PREFS_NAME = "secure_prefs";
    private static final String TAG = "SecurePreferences";

    public static SharedPreferences getEncryptedSharedPreferences(Context context) {
        // returning standard SharedPreferences
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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
